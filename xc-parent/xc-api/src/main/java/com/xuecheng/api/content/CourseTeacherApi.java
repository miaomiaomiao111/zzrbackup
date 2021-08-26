package com.xuecheng.api.content;


import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.api.content.model.vo.CourseTeacherVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Api("教师信息管理")
public interface CourseTeacherApi {

    @ApiOperation("根据课程id查询教师信息")
    @ApiImplicitParam(value = "courseBaseId",name = "课程Id",required = true,paramType = "path",dataType = "Long")
    List<CourseTeacherDTO> queryTeachers(@PathVariable("courseBaseId") Long courseBaseId, PageRequestParams params);
    @ApiImplicitParam(value = "courseTeacherVO",required = true,paramType = "body")
    CourseTeacherDTO modifyTeacher(@RequestBody CourseTeacherVO courseTeacherVO);
}
