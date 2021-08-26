package com.xuecheng.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.agent.order.ContentSearchApiAgent;
import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.api.learning.model.dto.CourseRecordDTO;
import com.xuecheng.api.search.model.dto.CoursePubIndexDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.enums.common.CommonEnum;
import com.xuecheng.common.enums.content.CourseChargeEnum;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.JsonUtil;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.learning.common.constant.LearningErrorCode;
import com.xuecheng.learning.convert.CourseRecordConvert;
import com.xuecheng.learning.entity.CourseRecord;
import com.xuecheng.learning.mapper.CourseRecordMapper;
import com.xuecheng.learning.service.CourseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 选课记录 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseRecordServiceImpl extends ServiceImpl<CourseRecordMapper, CourseRecord> implements CourseRecordService {
    @Autowired
    private ContentSearchApiAgent searchApiAgent;


    @Override
    public CourseRecordDTO getMyCourseRecord(String username, Long coursePubId) {

        //1.判断关键数据
        //     coursePubId username
        if (ObjectUtils.isEmpty(coursePubId) ||
                StringUtil.isBlank(username)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        // 2.根据条件查询学习记录
        LambdaQueryWrapper<CourseRecord> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(CourseRecord::getCoursePubId, coursePubId);
        queryWrapper.eq(CourseRecord::getUserName, username);

        CourseRecord courseRecord = this.getOne(queryWrapper);

        // 3.查询学习记录数据进行判断
        CourseRecordDTO dto = null;
        if (ObjectUtils.isEmpty(courseRecord)) {
            dto = new CourseRecordDTO();
        } else {
            dto = CourseRecordConvert.INSTANCE.entity2dto(courseRecord);
        }
        return dto;
    }

    @Override
    public void createOrModifyCourseRecord(CourseRecordDTO dto) {
        //1.判断关键数据
        //     username  coursepubid
        Long coursePubId = dto.getCoursePubId();
        String userName = dto.getUserName();
        if (StringUtil.isBlank(userName) ||
                ObjectUtils.isEmpty(coursePubId)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        // 2.判断业务数据
        //     课程发布数据
        //         判断课程发布数据是否存在
        //     课程计划数据（dto中如果有课程计划数据，不需判断此业务数据）
        //         如果课程是新课程并且用户第一次购买，给用户添加一个默认的学习记录（第一章的第一小节）

        RestResponse<CoursePubIndexDTO> restResponse = searchApiAgent.getCoursePubIndexById4s(coursePubId);

        if (!(restResponse.isSuccessful())) {
            ExceptionCast.cast(LearningErrorCode.E_202205);
        }

        CoursePubIndexDTO coursePubIndexDTO = restResponse.getResult();
        String teachplanJsonString = coursePubIndexDTO.getTeachplan();
        TeachplanDTO teachplanDTO = JsonUtil.jsonToObject(teachplanJsonString, TeachplanDTO.class);
        // 3.保存用户的学习记录
        //     判断用户对此课程是否有学习记录（username、coursePubid）
        //         如果有：修改学习记录中的teachplan数据（teachplanid、teachplanname）
        //         如果没有:创建用户的学习记录
        LambdaQueryWrapper<CourseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseRecord::getUserName, userName);
        queryWrapper.eq(CourseRecord::getCoursePubId, coursePubId);

        CourseRecord courseRecord = this.getOne(queryWrapper);
        if (ObjectUtils.isEmpty(courseRecord)) {

            // 如果没有:创建用户的学习记录
            courseRecord = new CourseRecord();

            courseRecord.setUserName(userName);
            courseRecord.setCompanyId(coursePubIndexDTO.getCompanyId());
            courseRecord.setCourseId(coursePubIndexDTO.getCourseId());
            courseRecord.setCoursePubId(coursePubId);
            courseRecord.setCoursePubName(coursePubIndexDTO.getName());
            courseRecord.setTeachmode(coursePubIndexDTO.getTeachmode());
            // 判断课程的收费情况
            /*
             * 课程收费情况：
             *   1.免费
             *       默认第一章的第一小节
             *   2.收费
             *       判断课程计划的第一章第一小节是否收费
             *          如果免费：第一章的第一小节
             *          如果是收费：后的用户对此收费课程的交易记录
             *                   发送feign请求获得orderPay数据
             * */

            String charge = coursePubIndexDTO.getCharge();

            if (CourseChargeEnum.CHARGE_NO.getCode().equals(charge)) {
                //1.免费
                //     默认第一章的第一小节
                List<TeachplanDTO> secTreeNode = teachplanDTO.getTeachPlanTreeNodes();
                TeachplanDTO threeTreeNode = secTreeNode.get(0).getTeachPlanTreeNodes().get(0);
                courseRecord.setTeachplanId(threeTreeNode.getTeachPlanId());
                courseRecord.setTeachplanName(threeTreeNode.getPname());

            } else {
                //2.收费
                //     判断课程计划的第一章第一小节是否收费
                //        如果免费：第一章的第一小节
                //        如果是收费：用户对此收费课程的交易记录
                //                 发送feign请求获得orderPay数据
                List<TeachplanDTO> secTreeNode = teachplanDTO.getTeachPlanTreeNodes();
                TeachplanDTO threeTreeNode = secTreeNode.get(0).getTeachPlanTreeNodes().get(0);
                String isPreview = threeTreeNode.getIsPreview();
//                if (CommonEnum.DELETE_FLAG.getCode().equals(isPreview)) {
//
//                    // 发送feign请求获得orderPay数据
//                    // TODO:
//                    ExceptionCast.cast(CommonErrorCode.E_100108);
//                }

                courseRecord.setTeachplanId(threeTreeNode.getTeachPlanId());
                courseRecord.setTeachplanName(threeTreeNode.getPname());
                courseRecord.setPaid(CommonEnum.USING_FLAG.getCodeInt());
                boolean result = this.save(courseRecord);

                ExceptionCast.cast(!result, LearningErrorCode.E_202204);
            }
        } else {
            // 如果有：修改学习记录中的teachplan数据（teachplanid、teachplanname）
            Long teachplanId = dto.getTeachplanId();
            String teachplanName = dto.getTeachplanName();
            if (ObjectUtils.isEmpty(teachplanId) ||
                    StringUtil.isBlank(teachplanName)
            ) {
                ExceptionCast.cast(CommonErrorCode.E_100101);
            }

            LambdaUpdateWrapper<CourseRecord> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(CourseRecord::getTeachplanId, teachplanId);
            updateWrapper.set(CourseRecord::getTeachplanName, teachplanName);
            updateWrapper.set(CourseRecord::getChangeDate, LocalDateTime.now());
            updateWrapper.eq(CourseRecord::getId, courseRecord.getId());

            boolean result = this.update(updateWrapper);

            ExceptionCast.cast(!result, LearningErrorCode.E_202204);

        }
    }
}
