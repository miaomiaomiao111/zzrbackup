package com.xuecheng.order.controller;

import com.xuecheng.order.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单支付信息 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
@RequestMapping("pay")
public class PayController {

    @Autowired
    private PayService  payService;
}
