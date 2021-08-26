package com.xuecheng.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.order.model.dto.OrdersDTO;
import com.xuecheng.api.order.model.pay.PayCodeUrlResult;
import com.xuecheng.api.order.model.pay.PayResultModel;
import com.xuecheng.order.entity.Orders;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author itcast
 * @since 2021-08-21
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 创建用户的课程订单数据
     * @param coursePubId
     * @return
     */
    OrdersDTO createOrder(Long coursePubId,String username);


    /**
     * 生成支付的地址
     * @param orderNo
     * @return
     */
    PayCodeUrlResult createPayCodeResult(String orderNo, String username);

    /**
     *  支付通知结果
     *  发送事务消息
     * @param xmlResult
     */
    void payNotifyResult(String xmlResult);

    /**
     * 处理已支付后的订单状态
     * @param payResultModel
     */
    void successPayment(PayResultModel payResultModel);
}
