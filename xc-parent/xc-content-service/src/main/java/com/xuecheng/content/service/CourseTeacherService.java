package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.content.entity.CourseTeacher;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务类
 * </p>
 *
 * @author itcast
 * @since 2021-08-13
 */
public interface CourseTeacherService extends IService<CourseTeacher> {

    /**
     * 根据课程Id查询教师信息
     * @param courseBaseId
     * @return
     */
    List<CourseTeacherDTO> queryTeachersByCourseBaseId(Long courseBaseId, Long companyId);

    /**
     * 新增/修改教师信息
     * @param courseTeacherDTO
     * @param companyId
     * @return
     */
    CourseTeacherDTO modifyTeacher(CourseTeacherDTO courseTeacherDTO, Long companyId);
}
