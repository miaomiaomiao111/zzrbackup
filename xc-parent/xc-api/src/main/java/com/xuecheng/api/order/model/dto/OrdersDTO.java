package com.xuecheng.api.order.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author itcast
 */
@Data
@ApiModel(value="OrdersDTO", description="订单")
public class OrdersDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long orderId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "课程Id")
    private Long coursePubId;

    @ApiModelProperty(value = "课程名称")
    private String coursePubName;

    @ApiModelProperty(value = "机构ID")
    private Long companyId;

    @ApiModelProperty(value = "机构名称")
    private String companyName;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "定价")
    private Float initialPrice;

    @ApiModelProperty(value = "交易价")
    private Float price;

    @ApiModelProperty(value = "课程有效性")
    private String valid;

    @ApiModelProperty(value = "起始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "交易状态（参照OrderDealStatusEnum枚举类）")
    private Integer status;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "修改日期")
    private LocalDateTime changeDate;


}
