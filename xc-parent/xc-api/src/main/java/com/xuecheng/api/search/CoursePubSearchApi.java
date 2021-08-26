package com.xuecheng.api.search;

import com.xuecheng.api.search.model.dto.CoursePubIndexDTO;
import com.xuecheng.api.search.model.qo.QueryCoursePubModel;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程发布搜索服务API管理")
public interface CoursePubSearchApi {

    @ApiOperation("根据条件分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryModel", value = "查询条件封装数据", required = false, dataType = "QueryCoursePubModel", paramType = "body")
    })
    PageVO<CoursePubIndexDTO> coursePubIndexByCondition(PageRequestParams pageParams, QueryCoursePubModel queryModel);

    @ApiOperation(value = "根据Id获得课程发布信息")
    @ApiImplicitParam(name = "coursePubId",
            value = "课程发布ID", required = true,
            dataType = "Long", paramType = "path", example = "1")
    CoursePubIndexDTO getCoursePubById(Long coursePubId);

    @ApiOperation("根据id查询课程发布索引数据")
    RestResponse<CoursePubIndexDTO> getCoursePubIndexById4s(Long coursePubId);
}