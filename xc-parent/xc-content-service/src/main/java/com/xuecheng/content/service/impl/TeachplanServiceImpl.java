package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.api.content.model.dto.TeachplanMediaDTO;
import com.xuecheng.api.media.agent.MediaApiAgent;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.enums.common.AuditEnum;
import com.xuecheng.common.enums.common.CommonEnum;
import com.xuecheng.common.enums.content.CourseAuditEnum;
import com.xuecheng.common.enums.content.CourseModeEnum;
import com.xuecheng.common.enums.content.TeachPlanEnum;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.content.common.constant.ContentErrorCode;
import com.xuecheng.content.convert.TeachplanConvert;
import com.xuecheng.content.convert.TeachplanMediaConvert;
import com.xuecheng.content.entity.CourseBase;
import com.xuecheng.content.entity.Teachplan;
import com.xuecheng.content.entity.TeachplanMedia;
import com.xuecheng.content.entity.ex.TeachplanNode;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.TeachplanMediaService;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private MediaApiAgent mediaApiAgent;

    @Autowired
    private TeachplanMediaService teachplanMediaService;

    @Override
    public TeachplanDTO queryTreeNodesByCourseId(Long courseId, Long companyId) {
        //1.判断关键数据
//        if(ObjectUtils.isEmpty(companyId) || ObjectUtils.isEmpty(courseId)){
//            ExceptionCast.cast(CommonErrorCode.E_100101);
//        }
        //传入参数与接口不匹配
        ExceptionCast.cast(ObjectUtils.isEmpty(companyId) || ObjectUtils.isEmpty(courseId), CommonErrorCode.E_100101);
        //2.判断业务数据
        //判断课程是否存在
        CourseBase courseBase = getCourseBase(courseId, companyId);
        ExceptionCast.cast(ObjectUtils.isEmpty(courseBase), ContentErrorCode.E_120013);

        //判断课程是否被删除
        Integer status = courseBase.getStatus();
        ExceptionCast.cast(CommonEnum.DELETE_FLAG.getCodeInt().equals(status), ContentErrorCode.E_120018);
        // 3.根据课程id查询课程计划集合数据
        TeachplanMapper teachplanMapper = this.getBaseMapper();
        // 根据课程 CourseBaseId 查询出相关的课程计划。
        // 使用 MP 的映射文件，将查询出的集合数据进行封装到 TeachplanNode 扩展类中。
        // 排序好的结果
        List<TeachplanNode> nodes = teachplanMapper.selectByCourseId(courseId);
        TeachplanDTO resultDTO = null;
        if (CollectionUtils.isEmpty(nodes)) {
            resultDTO = new TeachplanDTO();
        } else {
            // 4.通过java的递归来生成课程计划树形结构
            //移除一级计划
            TeachplanNode rootNode = nodes.remove(0);
            //递归
            generateTreeNodes(rootNode, nodes);
            // 5.将po转为dto数据并返回
            resultDTO = TeachplanConvert.INSTANCE.node2dto(rootNode);
        }
        return resultDTO;
    }

    @Override
    public TeachplanDTO createOrModifyTeachPlan(TeachplanDTO dto, Long companyId) {

        //获取TeachPlanId
        Long teachPlanId = dto.getTeachPlanId();

        TeachplanDTO resutlDTO = null;

        //如果课程计划没有id
        //新增操作
        if (ObjectUtils.isEmpty(teachPlanId)) {

            resutlDTO = createTeachplan(dto, companyId);

        } else {
            //如果课程计划有id
            //修改操作
            resutlDTO = modifyTeachplan(dto, companyId);
        }


        return resutlDTO;
    }


    //新增
    private TeachplanDTO createTeachplan(TeachplanDTO dto, Long companyId) {
        //1.判断关键数据
        Long courseId = dto.getCourseId();
        ExceptionCast.cast(ObjectUtils.isEmpty(companyId) || ObjectUtils.isEmpty(courseId), CommonErrorCode.E_100101);
        // 2.判断业务数据
        CourseBase courseBase = veriyCourseMsg(dto, companyId);
        // 3.获得当前课程计划的父级数据
        Teachplan parentNode = getParentNode(dto, courseBase);
        //4.获得父级的id值给当前的课程计划赋值为parentid
        Long parentId = parentNode.getId();
        dto.setParentid(parentId);
        //5在父级的等级基础上+1 获得当前课程计划的等级
        dto.setGrade(parentNode.getGrade() + 1);
        //  获得当前课程计划的orderby
        //  获得父节点数据中子节点数据的个数
        //  父节点数据中子节点数据的个数 + 1 = 当前node的orderby数据
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getParentid, parentId);
        int count = this.count(queryWrapper);
        dto.setOrderby(count + 1);
        // 6.保存课程计划数据并返回保存后的结果
        Teachplan teachplan = TeachplanConvert.INSTANCE.dto2entity(dto);
        boolean result = this.save(teachplan);
        //6.1判断保存后的结果
        if (!result) {
            ExceptionCast.cast(ContentErrorCode.E_120407);
        }
        // 5.将保存后的po数据转为dto数据
        TeachplanDTO resultDTO = TeachplanConvert.INSTANCE.entity2dto(teachplan);
        return resultDTO;
    }

    //获得一个课程计划的父级数据
    /*
    * 课程计划的二级和三级节点
        - 数据来源：教育机构人员在页面填写的数据
        - 创建时机：教育机构人员提交课程计划数据
        - 注意事项：添加时要校验父级菜单是否存在
          - 二级在添加时
            - 父级不存在（一级）：自动创建并获得获得父级Id值
            - 父级存在（一级）：获得父级Id值
          - 三级在添加时
            - 父级不存在（二级）：抛出异常
            - 父级存在（二级）：获得父级Id值
    * */
    private Teachplan getParentNode(TeachplanDTO dto, CourseBase courseBase) {
        //1.判断是一级还是二级
        Long parentId = dto.getParentid();
        // 如果添加课程计划没有parentId则是二级课程计划
        if (ObjectUtils.isEmpty(parentId)) {
            //获取当前课程以及课程计划
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getCourseId, courseBase.getId());
            queryWrapper.eq(Teachplan::getGrade, TeachPlanEnum.FIRST_LEVEL);
            queryWrapper.eq(Teachplan::getParentid, TeachPlanEnum.FIRST_PARENTID_FLAG);
            Teachplan rootNode = this.getOne(queryWrapper);
            //如果没有则创建一个一级课程计划
            if (ObjectUtils.isEmpty(rootNode)) {
                rootNode = new Teachplan();

                rootNode.setPname(courseBase.getName());
                rootNode.setParentid(Long.valueOf(TeachPlanEnum.FIRST_PARENTID_FLAG));
                rootNode.setGrade(TeachPlanEnum.FIRST_LEVEL);
                rootNode.setDescription(courseBase.getDescription());
                rootNode.setOrderby(TeachPlanEnum.FIRST_LEVEL);
                rootNode.setCourseId(courseBase.getId());

                boolean result = this.save(rootNode);

                ExceptionCast.cast(!result, ContentErrorCode.E_120407);
            }
            return rootNode;

        } else {// 如果添加课程计划有parentId则是三级课程计划
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getId, parentId);
            Teachplan secNode = this.getOne(queryWrapper);
            ExceptionCast.cast(ObjectUtils.isEmpty(secNode), ContentErrorCode.E_120408);

            return secNode;
        }
    }


    //修改
    private TeachplanDTO modifyTeachplan(TeachplanDTO dto, Long companyId) {
        Long courseId = dto.getCourseId();
        // 1.判断关键数据
        //  课程计划的名称：panme、
        //  课程id值：courseBaseId、
        //  父级id：parentId
        //  公司id：companyId
        if (StringUtil.isBlank(dto.getPname()) ||
                ObjectUtils.isEmpty(courseId) ||
                ObjectUtils.isEmpty(dto.getParentid()) ||
                ObjectUtils.isEmpty(companyId)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断课程业务数据
        CourseBase courseBase = veriyCourseMsg(dto, companyId);
        //3.判断课程计划的业务数据
        Long teachPlanId = dto.getTeachPlanId();
        //判断修改的课程计划是否存在
        Teachplan teachplan = this.getById(teachPlanId);
        ExceptionCast.cast(ObjectUtils.isEmpty(teachplan), ContentErrorCode.E_120402);
        // 4.修改课程计划数据
        // 不允许修改的数据内容：
        //     parentid
        //     grade
        //     orderBy
        //     courseId
        Teachplan po = TeachplanConvert.INSTANCE.dto2entity(dto);

        po.setParentid(teachplan.getParentid());
        po.setGrade(teachplan.getGrade());
        po.setOrderby(teachplan.getOrderby());
        po.setCourseId(teachplan.getCourseId());
        //5.修改数据
        boolean result = this.updateById(po);
        //6.判断修改后的结果
        ExceptionCast.cast(!result, ContentErrorCode.E_120407);
        //7.返回数据内容dto
        TeachplanDTO resultDTO = TeachplanConvert.INSTANCE.entity2dto(po);
        return resultDTO;
    }

    /**
     * 判断业务数据
     * 课程基础信息
     * 判断是否存在
     * 判断是否是同一家机构
     * 判断是否删除
     * 判断课程的审核状态：未提交、审核未通过
     * 1 判断课程的业务数据
     * <p>
     * 课程基础信息
     *
     * @param dto
     * @param companyId
     * @return
     */

    private CourseBase veriyCourseMsg(TeachplanDTO dto, Long companyId) {
        CourseBase courseBase = getCourseBase(dto.getCourseId(), companyId);

        //  判断是否存在 判断是否是同一家机构
        ExceptionCast.cast(ObjectUtils.isEmpty(courseBase), ContentErrorCode.E_120013);


        // 判断是否删除
        ExceptionCast.cast(CommonEnum.DELETE_FLAG.getCode().equals(courseBase.getStatus()),
                ContentErrorCode.E_120018);

        // 判断课程的审核状态：未提交、审核未通过
        String auditStatus = courseBase.getAuditStatus();
        if (CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_PASTED_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus)
        ) {
            ExceptionCast.cast(ContentErrorCode.E_120015);
        }
        return courseBase;
    }

    /*
     * 通过java的递归来生成课程计划树形结构
     * */
    private void generateTreeNodes(TeachplanNode rootNode, List<TeachplanNode> nodes) {

        // 1.判断父节点中的集合数据是否为空，如果为空，创建出集合数据
        if (CollectionUtils.isEmpty(rootNode.getChildrenNodes())) {
            rootNode.setChildrenNodes(new ArrayList<>());
        }

        // 2.遍历集合数据进行递归并生成树形结构
        for (TeachplanNode node : nodes) {

            // 通过子节点中的parentid == 父节点的id值
            if (ObjectUtils.nullSafeEquals(rootNode.getId(), node.getParentid())) {

                rootNode.getChildrenNodes().add(node);

                if (!(TeachPlanEnum.THIRD_LEVEL.equals(node.getGrade()))) {
                    generateTreeNodes(node, nodes);
                }else {
                    // 如果课程是第三级，查询课程计划是否有关联的媒资信息
                    LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(TeachplanMedia::getTeachplanId, node.getId());
                    TeachplanMedia teachplanMedia = teachplanMediaService.getOne(queryWrapper);
                    node.setTeachplanMedia(teachplanMedia);
                }

            }
        }
    }

    private CourseBase getCourseBase(Long courseBaseId, Long companyId) {
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(CourseBase::getId, courseBaseId);
        queryWrapper.eq(CourseBase::getCompanyId, companyId);

        CourseBase courseBase = courseBaseService.getOne(queryWrapper);
        return courseBase;
    }

    @Override
    public TeachplanMediaDTO associateMedia(TeachplanMediaDTO dto, Long companyId) {
        // 1.判断关键数据
        //      mediaId、teachplanid、companyId
        if (ObjectUtils.isEmpty(dto.getMediaId()) ||
                ObjectUtils.isEmpty(dto.getTeachplanId()) ||
                ObjectUtils.isEmpty(companyId)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断业务数据：coursebase、teachplan
        CourseBase courseBase = verifyCourseAndTeachplanMsg(dto, companyId);
        //3.获取媒资数据并判断
        MediaDTO mediaDTO = getMediaDTO(dto, companyId);
        //4.绑定课程计划和媒资信息数据
        //   断添加或修改
        //      courseId和teachplanid查询
        //          添加：一个课程计划没有teachplanMedia数据
        //          修改：课程一个课程计划和mediaid数据
        //                  mediaId
        //                  mediaFileName
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanMedia::getTeachplanId, dto.getTeachplanId());
        queryWrapper.eq(TeachplanMedia::getCourseId, courseBase.getId());
        TeachplanMedia teachplanMedia = teachplanMediaService.getOne(queryWrapper);
        //没有则添加
        if (ObjectUtils.isEmpty(teachplanMedia)) {
            teachplanMedia = new TeachplanMedia();
            teachplanMedia.setMediaId(dto.getMediaId());
            teachplanMedia.setTeachplanId(dto.getTeachplanId());
            teachplanMedia.setCourseId(courseBase.getId());
            teachplanMedia.setMediaFilename(mediaDTO.getFilename());
            teachplanMedia.setCreateDate(LocalDateTime.now());
            //判断保存结果
            boolean result = teachplanMediaService.save(teachplanMedia);
            ExceptionCast.cast(!result, ContentErrorCode.E_120411);
        } else {
            //修改：课程一个课程计划和mediaid数据
            //  mediaId
            //  mediaFileName
            teachplanMedia.setMediaId(mediaDTO.getId());
            teachplanMedia.setMediaFilename(mediaDTO.getFilename());

            boolean result = teachplanMediaService.updateById(teachplanMedia);

            ExceptionCast.cast(!result, ContentErrorCode.E_120411);
            //判断操作结果并返回数据dto
            TeachplanMediaDTO resultDTO = TeachplanMediaConvert.INSTANCE.entity2dto(teachplanMedia);

            return resultDTO;
        }
        return null;
    }

    private MediaDTO getMediaDTO(TeachplanMediaDTO dto, Long companyId) {
        //是否存在
        RestResponse<MediaDTO> restResponse = mediaApiAgent.getById(dto.getMediaId());
        if (!(restResponse.isSuccessful())) {
            ExceptionCast.castWithCodeAndDesc(restResponse.getCode(), restResponse.getMsg());
        }
        // 判断媒资是否审核通过
        MediaDTO mediaDTO = restResponse.getResult();
        String mediaAuditStatus = mediaDTO.getAuditStatus();
        ExceptionCast.cast(!(AuditEnum.AUDIT_PASTED_STATUS.getCode().equals(mediaAuditStatus)),
                ContentErrorCode.E_120416);

        // 判断是否是同一家机构
        ExceptionCast.cast(!(ObjectUtils.nullSafeEquals(mediaDTO.getCompanyId(), companyId)),
                CommonErrorCode.E_100108);
        return mediaDTO;
    }

    /**
     * 2.判断业务数据
     * 课程基本信息（通过teachplan中的courseId）
     * 判断课程信息是否存在
     * 判断课程是否删除
     * 判断课程的审核状态：未提交、审核未通过
     * 判断是否是同一家机构
     * 判断课程是否是录播课程（只有录播课程才可以绑定资源）
     * 课程计划
     * 判断是否存在
     * 判断课程计划是否是第三级
     * 媒资信息
     * 判断是否存在
     * 判断媒资是否审核通过
     * 判断是否是同一家机构
     * <p>
     * .1 课程计划
     * 判断是否存在
     * 判断课程计划是否是第三级
     */
    private CourseBase verifyCourseAndTeachplanMsg(TeachplanMediaDTO dto, Long companyId) {
        //获取课程计划
        Teachplan teachplan = this.getById(dto.getTeachplanId());
        // 判断课程计划是否存在
        if (ObjectUtils.isEmpty(teachplan)) {
            ExceptionCast.cast(ContentErrorCode.E_120402);
        }
        // 判断课程计划是否是第三级
        if (!(TeachPlanEnum.THIRD_LEVEL.equals(teachplan.getGrade()))) {
            ExceptionCast.cast(ContentErrorCode.E_120410);
        }
        // 2.2  课程基本信息（通过teachplan中的courseId）
        //      判断课程信息是否存在,判断是否是同一家机构
        //      判断课程是否删除
        //      判断课程的审核状态：未提交、审核未通过
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseBase::getId, teachplan.getCourseId());
        queryWrapper.eq(CourseBase::getCompanyId, companyId);
        // 判断课程信息是否存在,是否是同一家机构
        CourseBase courseBase = courseBaseService.getOne(queryWrapper);

        if (ObjectUtils.isEmpty(courseBase)) {
            ExceptionCast.cast(ContentErrorCode.E_120013);
        }

        // 判断课程是否删除
        ExceptionCast.cast(CommonEnum.DELETE_FLAG.getCodeInt().equals(courseBase.getStatus()),
                ContentErrorCode.E_120018);
        // 判断课程的审核状态：未提交、审核未通过才可以绑定
        String auditStatus = courseBase.getAuditStatus();
        if (CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_PASTED_STATUS.getCode().equals(auditStatus)
        ) {
            ExceptionCast.cast(ContentErrorCode.E_120015);
        }
        // 判断课程是否是录播课程（只有录播课程才可以绑定资源）
        String teachmode = courseBase.getTeachmode();
        if (!(CourseModeEnum.COURSE_MODE_RECORD_STATUS.getCode().equals(teachmode))) {
            ExceptionCast.cast(ContentErrorCode.E_120415);
        }


        return courseBase;
    }

}

