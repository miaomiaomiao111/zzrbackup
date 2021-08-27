package com.xuecheng.content.controller;

import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import com.xuecheng.content.entity.CoursePub;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xuecheng.content.service.CoursePubService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 课程发布 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
@RequestMapping
public class CoursePubController {

    @Autowired
    private CoursePubService  coursePubService;

    @GetMapping("l/coursePub/{targetId}")
    CoursePub getCompInfoDetail(@PathVariable(value = "targetId") Long targetId){
        CoursePub coursePub = coursePubService.getById(targetId);
        return coursePub;
    }
}
