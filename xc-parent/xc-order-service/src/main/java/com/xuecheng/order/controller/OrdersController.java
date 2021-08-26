package com.xuecheng.order.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.xuecheng.api.order.OrderApi;
import com.xuecheng.api.order.model.dto.OrdersDTO;
import com.xuecheng.api.order.model.pay.PayCodeUrlResult;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.uaa.LoginUser;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.order.common.utils.UAASecurityUtil;
import com.xuecheng.order.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class OrdersController implements OrderApi {

    @Autowired
    private OrdersService ordersService;

    @GetMapping("orders/create/{coursePubId}")
    public OrdersDTO createOrder(@PathVariable Long coursePubId) {

        LoginUser user = UAASecurityUtil.getUser();

        ExceptionCast.cast(ObjectUtils.isEmpty(user), CommonErrorCode.E_100108);

        return ordersService.createOrder(coursePubId, user.getUsername());
    }

    @GetMapping("orderPay/wxPay/createPay")
    public PayCodeUrlResult createPayCodeResult(String orderNo) {

        LoginUser user = UAASecurityUtil.getUser();

        ExceptionCast.cast(ObjectUtils.isEmpty(user), CommonErrorCode.E_100108);

        return ordersService.createPayCodeResult(orderNo, user.getUsername());
    }

    @RequestMapping("order-pay/wx-pay/notify-result")
    public String payNotifyResult(HttpServletRequest request, HttpServletResponse response) {
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            //执行service方法发送事务消息
            ordersService.payNotifyResult(xmlResult);
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }
}
