package com.xuecheng.content.controller;


import com.xuecheng.api.content.CourseTeacherApi;
import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.api.content.model.vo.CourseTeacherVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.util.SecurityUtil;
import com.xuecheng.content.convert.CourseTeacherConvert;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class CourseTeacherController implements CourseTeacherApi {

    @Autowired
    private CourseTeacherService courseTeacherService;

    @GetMapping(value = "courseTeacher/list/{courseBaseId}", name = "查询课程教师信息")
    public List<CourseTeacherDTO> queryTeachers(@PathVariable("courseBaseId") Long courseBaseId,
                                               PageRequestParams params) {

        Long companyId = SecurityUtil.getCompanyId();
        List<CourseTeacherDTO> dtos = courseTeacherService.queryTeachersByCourseBaseId(courseBaseId, companyId);
        return dtos;
    }

    @PostMapping(value = "courseTeacher", name = "新增/修改教师信息")
    public CourseTeacherDTO modifyTeacher(@RequestBody CourseTeacherVO courseTeacherVO) {

        Long companyId = SecurityUtil.getCompanyId();
        CourseTeacherDTO courseTeacherDTO = CourseTeacherConvert.INSTANCE.vo2dto(courseTeacherVO);
        CourseTeacherDTO dto = courseTeacherService.modifyTeacher(courseTeacherDTO, companyId);
        return dto;
    }

}
