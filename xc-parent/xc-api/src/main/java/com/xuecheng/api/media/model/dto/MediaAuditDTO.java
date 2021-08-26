package com.xuecheng.api.media.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MediaAuditDTO {

    @ApiModelProperty(value = "媒资I")
    private Long id;

    @ApiModelProperty(value = "审核状态：参照数据字典 code为 202")
    private String auditStatus;

    @ApiModelProperty(value = "审核意见")
    private String auditMind;
}
