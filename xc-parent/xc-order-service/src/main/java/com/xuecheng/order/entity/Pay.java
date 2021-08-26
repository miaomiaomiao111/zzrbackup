package com.xuecheng.order.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("order_pay")
public class Pay implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 机构ID
     */
    private Long companyId;

    /**
     * 订单号
     */
    private Long orderId;

    /**
     * 支付系统订单号
     */
    private String payNumber;

    /**
     * 交易状态（0：未支付，1：已支付）
     */
    private String status;

    /**
     * 支付方式（ALI,WX）
     */
    private String payMethod;

    /**
     * 支付时间
     */
    private LocalDateTime payDate;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 实收金额
     */
    private BigDecimal receiptAmount;

    /**
     * 付款金额
     */
    private BigDecimal buyerPayAmount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 支付响应
     */
    private String payResponse;


}
