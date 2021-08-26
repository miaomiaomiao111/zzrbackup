package com.xuecheng.api.search.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value="CoursePubIndexDTO", description="课程发布")
public class CoursePubIndexDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long indexId;

    @ApiModelProperty(hidden = true)
    private Long course_id;

    @ApiModelProperty(value = "课程标识",example = "1")
    public Long getCourseId() {
        return course_id;
    }

    @ApiModelProperty(hidden = true)
    private Long company_id;

    @ApiModelProperty(value = "机构ID",example = "1")
    public Long getCompanyId() {
        return company_id;
    }

    @ApiModelProperty(hidden = true)
    private String company_name;

    @ApiModelProperty(value = "公司名称")
    public String getCompanyName() {
        return company_name;
    }

    @ApiModelProperty(value = "课程名称")
    private String name;

    @ApiModelProperty(value = "适用人群")
    private String users;

    @ApiModelProperty(value = "标签")
    private String tags;

    @ApiModelProperty(value = "大分类")
    private String mt;

    @ApiModelProperty(value = "大分类名称")
    private String mtName;

    @ApiModelProperty(value = "小分类")
    private String st;

    @ApiModelProperty(value = "小分类名称")
    private String stName;

    @ApiModelProperty(value = "课程等级")
    private String grade;

    @ApiModelProperty(value = "教育模式(common普通，record 录播，live直播等）")
    private String teachmode;

    @ApiModelProperty(value = "课程图片")
    private String pic;

    @ApiModelProperty(value = "课程介绍")
    private String description;

    @ApiModelProperty(value = "所有课程计划，json格式")
    private String teachplan;

    @ApiModelProperty(hidden = true)
    private Date create_date;

    @ApiModelProperty(value = "发布时间")
    public Date getCreateDate() {
        return create_date;
    }

    @ApiModelProperty(hidden = true)
    private Date change_date;

    @ApiModelProperty(value = "修改时间")
    public Date getChangeDate() {
        return change_date;
    }

    @ApiModelProperty(hidden = true)
    private Integer is_latest;

    @ApiModelProperty(value = "是否最新课程(1最新)")
    public Integer getIsLatest() {
        return is_latest;
    }

    @ApiModelProperty(hidden = true)
    private Integer is_pub;

    @ApiModelProperty(value = "是否发布(1发布 0取消发布)")
    public Integer getIsPub() {
        return is_pub;
    }

    @ApiModelProperty(value = "状态（1正常  0删除）")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "课程营销数据")
    private String market;
    
    @ApiModelProperty(value = "收费规则，对应数据字典--203")
    private String charge;

    @ApiModelProperty(value = "现价")
    private Float price;

    @ApiModelProperty(value = "有效性，对应数据字典--204")
    private String valid;

    @ApiModelProperty(value = "学习人数")
    private Long learners;
    
    @ApiModelProperty(value = "课程评论数")
    private Long comment_num;
}