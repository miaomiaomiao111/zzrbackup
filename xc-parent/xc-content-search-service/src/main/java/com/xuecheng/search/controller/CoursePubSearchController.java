package com.xuecheng.search.controller;

import com.xuecheng.api.search.CoursePubSearchApi;
import com.xuecheng.api.search.model.dto.CoursePubIndexDTO;
import com.xuecheng.api.search.model.qo.QueryCoursePubModel;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.search.service.CoursePubSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 课程搜索服务控制层
 */
@Slf4j
@RestController
@RequestMapping
public class CoursePubSearchController implements CoursePubSearchApi {

    @Resource
    private CoursePubSearchService couresPubSearchService;

    @Autowired
    private CoursePubSearchService coursePubSearchService;

    @PostMapping("course_index")
    public PageVO<CoursePubIndexDTO> coursePubIndexByCondition(PageRequestParams pageParams, @RequestBody QueryCoursePubModel queryModel) {
        PageVO<CoursePubIndexDTO> pageVO = null;
        try {
            pageVO = couresPubSearchService.queryCoursePubIndex(pageParams, queryModel);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return pageVO;
    }



    @GetMapping("course_index/{coursePubId}")
    public CoursePubIndexDTO getCoursePubById(@PathVariable Long coursePubId) {
        return couresPubSearchService.getCoursePubIndexById(coursePubId);
    }


    @GetMapping("l/course-index/{coursePubId}")
    public RestResponse<CoursePubIndexDTO> getCoursePubIndexById4s(@PathVariable Long coursePubId) {
        return couresPubSearchService.getCoursePubIndexById4s(coursePubId);
    }
}