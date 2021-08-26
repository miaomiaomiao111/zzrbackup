package com.xuecheng.api.search.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "QueryCoursePubModel",description = "课程索引搜索条件查询对象")
public class QueryCoursePubModel {

    @ApiModelProperty("查询关键字")
    private String keyword;

    @ApiModelProperty("课程二级分类")
    private String mt;

    @ApiModelProperty("课程三级分类")
    private String st;

    @ApiModelProperty("课程等级")
    private String grade;

    @ApiModelProperty("排序字段, 推荐/最新/热评")
    private String sortFiled;
}