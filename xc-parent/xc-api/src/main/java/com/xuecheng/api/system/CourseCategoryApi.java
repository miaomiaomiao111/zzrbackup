package com.xuecheng.api.system;

import com.xuecheng.api.system.model.dto.CourseCategoryDTO;
import com.xuecheng.common.domain.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(value = "课程分类服务Api" ,tags = "系统-课程分类服务" ,
     description = "对课程分类信息业务操作")
public interface CourseCategoryApi {


    @ApiOperation("查询课程分类树形结构数据")
    List<CourseCategoryDTO> queryTreeNodes();

    @ApiOperation("根据id查询课程分类")
    RestResponse<CourseCategoryDTO> getCourseCategoryById(String id);

}