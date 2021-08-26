package com.xuecheng.api.content.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *     课程页面视图信息类
 * </p>
 *
 * @Description: 课程页面视图信息类用于页面对课程基本信息添加和修改，操作的属性比较有限，不开发的属性不让操作。
 */
@Data
@ApiModel(value="CourseBaseVO", description="课程基本信息视图类，用于页面对课程基本信息添加和修改，操作的属性比较有限，不开放的属性不让操作")
public class CourseBaseVO {

    @ApiModelProperty(value = "课程Id")
    private Long courseBaseId;

    @ApiModelProperty(value = "课程名称", required = true)
    private String name;

    @ApiModelProperty(value = "适用人群", required = true)
    private String users;

    @ApiModelProperty(value = "课程标签")
    private String tags;

    @ApiModelProperty(value = "大分类", required = true)
    private String mt;

    @ApiModelProperty(value = "小分类", required = true)
    private String st;

    @ApiModelProperty(value = "课程等级", required = true)
    private String grade;

    @ApiModelProperty(value = "教学模式（普通，录播，直播等）", required = true)
    private String teachmode;

    @ApiModelProperty(value = "课程介绍")
    private String description;

    @ApiModelProperty(value = "课程图片", required = true)
    private String pic;


    @ApiModelProperty(value = "收费规则，对应数据字典", required = true)
    private String charge;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

}