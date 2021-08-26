package com.xuecheng.api.order.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单支付信息
 * </p>
 *
 * @author itcast
 */
@Data
@ApiModel(value="PayDTO", description="订单支付信息")
public class PayDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "用户账号")
    private String userName;

    @ApiModelProperty(value = "机构ID")
    private Long companyId;

    @ApiModelProperty(value = "订单号")
    private Long orderId;

    @ApiModelProperty(value = "支付系统订单号")
    private String payNumber;

    @ApiModelProperty(value = "交易状态（0：未支付，1：已支付）")
    private String status;

    @ApiModelProperty(value = "支付方式（ALI,WX）")
    private String payMethod;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payDate;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "实收金额")
    private BigDecimal receiptAmount;

    @ApiModelProperty(value = "付款金额")
    private BigDecimal buyerPayAmount;

    private LocalDateTime createDate;

    @ApiModelProperty(value = "支付响应")
    private String payResponse;


}
