package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.api.content.model.vo.CourseTeacherVO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.enums.common.CommonEnum;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.content.common.constant.ContentErrorCode;
import com.xuecheng.content.convert.CourseTeacherConvert;
import com.xuecheng.content.entity.CourseBase;
import com.xuecheng.content.entity.CourseTeacher;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.CourseTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.execchain.TunnelRefusedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {
    @Autowired
    private CourseBaseService courseBaseService;
    @Override
    public List<CourseTeacherDTO> queryTeachersByCourseBaseId(Long courseBaseId, Long companyId) {

        //1.判断关键数据
        if (ObjectUtils.isEmpty(courseBaseId) ||
                ObjectUtils.isEmpty(companyId)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //判断课程是否存在
        CourseBase courseBase = courseBaseService.getById(courseBaseId);
        ExceptionCast.cast(ObjectUtils.isEmpty(courseBase), ContentErrorCode.E_120013);
        //判断课程是否删除
        //判断课程信息是否已删除
        if (CommonEnum.DELETE_FLAG.getCodeInt().equals(courseBase.getStatus())) {
            ExceptionCast.cast(ContentErrorCode.E_120018);
        }
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId,courseBaseId);
        List<CourseTeacher> courseTeachers = this.list(queryWrapper);
        List<CourseTeacherDTO> courseTeacherDTOS = CourseTeacherConvert.INSTANCE.entity2dtos(courseTeachers);
        return courseTeacherDTOS;
    }

    @Override
    public CourseTeacherDTO modifyTeacher(CourseTeacherDTO courseTeacherDTO, Long companyId) {
        //1.判断关键数据
        if (ObjectUtils.isEmpty(courseTeacherDTO.getCourseId()) ||
                ObjectUtils.isEmpty(companyId)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断是否是同一机构
        //判断课程是否存在 and 是否是同一家机构
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(CourseBase::getId, courseTeacherDTO.getCourseId());
        queryWrapper.eq(CourseBase::getCompanyId, companyId);

        CourseBase courseBase = courseBaseService.getOne(queryWrapper);
        if (ObjectUtils.isEmpty(courseBase)) {
            ExceptionCast.cast(ContentErrorCode.E_120013);
        }

        //判断新增还是修改
        Long teccherId = courseTeacherDTO.getCourseTeacherId();
        boolean flag = true;
        CourseTeacher courseTeacher = CourseTeacherConvert.INSTANCE.dto2entity(courseTeacherDTO);
        //没有新增
        if (ObjectUtils.isEmpty(teccherId)){
            this.save(courseTeacher);
        }else {
            this.updateById(courseTeacher);
        }
        return courseTeacherDTO;
    }
}
