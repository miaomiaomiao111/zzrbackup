package com.xuecheng.content.controller;

import com.xuecheng.api.content.CourseAuditApi;
import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.vo.CourseAuditVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.content.convert.CourseBaseConvert;
import com.xuecheng.content.model.qo.QueryCourseModel;
import com.xuecheng.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CourseAuditController implements CourseAuditApi {


    @Autowired
    private CourseBaseService courseBaseService;

    @PostMapping("m/course/list")
    public PageVO<CourseBaseDTO> queryCourseList(PageRequestParams params, @RequestBody QueryCourseModel model) {

        PageVO pageVO = courseBaseService.queryCourseList(params, model, null);

        return pageVO;
    }

    @PostMapping("m/courseReview/approve")
    public void approveCourse(@RequestBody CourseAuditVO auditVO) {

        //将 vo 转为 dto 数据
        CourseBaseDTO dto = CourseBaseConvert.INSTANCE.vo2dto(auditVO);

        //调用service方法进行课程审核
        courseBaseService.approve(dto);
    }
}