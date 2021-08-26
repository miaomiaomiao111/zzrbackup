package com.xuecheng.api.content;

import com.xuecheng.api.content.model.vo.CourseAuditVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.content.model.qo.QueryCourseModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程基本信息管理-运营 Api",tags = "课程基本信息管理-运营")
public interface CourseAuditApi {

    @ApiOperation("课程基础信息条件分页查询-运营")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "model",dataType = "QueryCourseModel",paramType = "body")
    })
    PageVO queryCourseList(PageRequestParams params, QueryCourseModel model);

    @ApiOperation("课程审核(提交的课程才可审核)")
    @ApiImplicitParam(name = "auditVO",
            value = "课程信息VO",
            required = true, dataType = "CourseAuditVO", paramType = "body")
    void approveCourse(CourseAuditVO auditVO);
}