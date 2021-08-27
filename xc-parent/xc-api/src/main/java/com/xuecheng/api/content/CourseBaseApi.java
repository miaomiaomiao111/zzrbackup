package com.xuecheng.api.content;


import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.vo.CourseBaseVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.content.model.qo.QueryCourseModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *     课程基本信息Api接口
 * </p>
 *
 * @Description:
 */
@Api(value = "课程基本信息Api接口",tags = "内容-课程基本业务信息Api接口",description = "课程基本业务信息Api接口")
public interface CourseBaseApi {

    /**
     * 分页条件查询课程基本信息
     * @param params
     * @param model
     * @return
     */
    @ApiOperation("分页条件查询课程基本信息")
    PageVO queryCourseList(PageRequestParams params, QueryCourseModel model);

    @ApiOperation(value = "保存课程基本信息")
    @ApiImplicitParam(name = "courseBaseVO", value = "课程基本视图信息", required = true, dataType = "CourseBaseVO", paramType = "body")
    CourseBaseDTO createCourseBase(CourseBaseVO courseBaseVO);


    @ApiOperation(value = "根据Id获取课程基本信息")
    @ApiImplicitParam(name = "courseBaseId", value = "课程基本信息ID", required = true, dataType = "Long", paramType = "path")
    CourseBaseDTO getCourseBase(Long courseBaseId);

    @ApiOperation("更新课程基本信息")
    @ApiImplicitParam(name = "courseBaseVO", value = "课程基本信息VO", required = true, dataType = "CourseBaseVO", paramType = "body")
    CourseBaseDTO modifyCourseBase(CourseBaseVO courseBaseVO);

    @ApiOperation("根据Id删除课程信息")
    @ApiImplicitParam(name = "courseBaseId", value = "课程id值", required = true, paramType = "path")
    void removeCoursebase(Long courseBaseId);

    @ApiOperation(value = "提交审核")
    @ApiImplicitParam(name = "courseBaseId", value = "课程ID", required = true, dataType = "Long", paramType = "query")
    void commitCourseBase(Long courseBaseId);

    @ApiOperation(value = "课程预览")
    Object preview(Long courseBaseId,Long companyId);


    @ApiOperation(value = "课程发布")
    void publish(Long courseId);
}