package com.xuecheng.api.learning;

import com.xuecheng.api.learning.model.dto.CourseRecordDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p></p>
 *
 * @Description:
 */
@Api(value = "用户的学习课程（选课）列表、 一个课程的学习情况 及更新课程的进度")
public interface CourseRecordApi {

    @ApiOperation(value = "查询用户某课程记录(获取某课程学习进度)")
    @ApiImplicitParam(name = "coursePubId", value = "课程发布Id", required = true, paramType = "path", example = "1")
    CourseRecordDTO getRecordByCoursePubId(Long coursePubId);
}
