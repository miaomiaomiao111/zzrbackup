package com.xuecheng.content.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *     课程基本信息查询条件封装 QO （query object）
 * </p>
 *
 * @Description: 根据课程名称、审核状态条件进行数据查询
 */
@Data
@ApiModel("课程基础信息查询QO对象")
public class QueryCourseModel {

    @ApiModelProperty("课程审核状态")
    private String auditStatus;

    @ApiModelProperty("课程名称")
    private String courseName;
}