package com.xuecheng.api.order.model.pay;

import com.xuecheng.api.order.model.dto.OrdersDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(value="PayResultModel", description="支付结果")
public class PayResultModel {

    @ApiModelProperty("学成订单数据")
    private OrdersDTO order;

    @ApiModelProperty("支付方式")
    private String payMethod;

    @ApiModelProperty("支付时间")
    private LocalDateTime payDate;

    @ApiModelProperty("微信支付订单号")
    private String payNumber;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;
    @ApiModelProperty("订单应结订单金额")
    private BigDecimal receiptAmount;
    @ApiModelProperty("订单现金支付金额")
    private BigDecimal buyerPayAmount;


    @ApiModelProperty("第三方支付响应结果")
    private String payResponse;

    @ApiModelProperty("交易状态")
    private String tradeStatus;
}