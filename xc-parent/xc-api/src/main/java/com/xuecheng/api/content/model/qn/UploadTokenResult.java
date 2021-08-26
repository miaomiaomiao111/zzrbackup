package com.xuecheng.api.content.model.qn;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @Description:
 */
@Data
@ApiModel("七牛获得上传令牌封装类")
public class UploadTokenResult {

    @ApiModelProperty("令牌类型-1：获得上传令牌")
    private String tokenType;

    @ApiModelProperty("七牛云存储空间")
    private String scope;

    @ApiModelProperty("文件的key值")
    private String key;
    @ApiModelProperty("令牌")
    private String qnToken;
    @ApiModelProperty("上传文件的地址-传递给前端")
    private String up_region;
    @ApiModelProperty("cdn域名")
    private String domain;

    @ApiModelProperty("令牌有效期（单位：秒）")
    private Integer deadline;
}