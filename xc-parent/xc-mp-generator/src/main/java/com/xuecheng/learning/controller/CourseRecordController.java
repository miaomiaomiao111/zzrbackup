package com.xuecheng.learning.controller;

import com.xuecheng.learning.service.CourseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 选课记录 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
@RequestMapping("courseRecord")
public class CourseRecordController {

    @Autowired
    private CourseRecordService  courseRecordService;
}
