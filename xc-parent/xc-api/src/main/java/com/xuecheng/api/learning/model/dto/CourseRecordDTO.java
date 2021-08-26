package com.xuecheng.api.learning.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 选课记录
 * </p>
 *
 * @author itcast
 */
@Data
@ApiModel(value="CourseRecordDTO", description="选课记录")
public class CourseRecordDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long courseRecordId;

    private Long userId;

    @ApiModelProperty(value = "选课人")
    private String userName;

    @ApiModelProperty(value = "课程所属机构标识")
    private Long companyId;

    @ApiModelProperty(value = "课程标识")
    private Long courseId;

    @ApiModelProperty(value = "课程发布ID")
    private Long coursePubId;

    @ApiModelProperty(value = "课程名称")
    private String coursePubName;

    @ApiModelProperty(value = "教育模式(common普通，record 录播，live直播等）")
    private String teachmode;

    @ApiModelProperty(value = "课程有效期（不论点播或直播，在该期限内有效）")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "课程有效期（不论点播或直播，在该期限内有效）")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "正在学习的课程计划章节Id")
    private Long teachplanId;

    @ApiModelProperty(value = "正在学习的课程计划章节名称")
    private String teachplanName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    private LocalDateTime changeDate;

    @ApiModelProperty(value = "是否已支付")
    private Integer paid;


}
