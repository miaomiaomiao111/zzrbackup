package com.xuecheng.api.media.model.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryMediaModel {
    private String filename;
    private String type;

    @ApiModelProperty("审核状态")
    String auditStatus;
    @ApiModelProperty("公司id")
    Long companyId;
}
