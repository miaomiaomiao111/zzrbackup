package com.xuecheng.order.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.xuecheng.api.order.model.dto.OrdersDTO;
import com.xuecheng.api.order.model.pay.PayResultModel;
import com.xuecheng.common.enums.order.OrderDealStatusEnum;
import com.xuecheng.common.util.JsonUtil;
import com.xuecheng.order.entity.Orders;
import com.xuecheng.order.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQTransactionListener
public class OrderPayTxMsgListener implements RocketMQLocalTransactionListener {

    @Autowired
    private OrdersService ordersService;


    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {

        try {

            // 1.获得事务发送的信息并解析
            String jsonString = new String((byte[]) message.getPayload());

            PayResultModel payResultModel = JsonUtil.jsonToObject(jsonString, PayResultModel.class);


            // 2.调用service更新订单状态
            ordersService.successPayment(payResultModel);

            // 3.返回事务提交
            return RocketMQLocalTransactionState.COMMIT;

        } catch (Exception e) {

            log.error("更新订单支付状态失败",e);

            // 3.返回事务回滚
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {

        // 1.获得事务发送的信息并解析
        String jsonString = new String((byte[]) message.getPayload());

        PayResultModel payResultModel = JsonUtil.jsonToObject(jsonString, PayResultModel.class);

        // 2.构建订单查询对象
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 设置更新条件（根据订单Id）
        OrdersDTO ordersDTO = payResultModel.getOrder();
        ordersLambdaQueryWrapper.eq(Orders::getId, ordersDTO.getOrderId());
        // 3.查询订单信息
        Orders orders = ordersService.getOne(ordersLambdaQueryWrapper);

        // 4.判断订单信息
        if (ObjectUtils.isEmpty(orders))
            return RocketMQLocalTransactionState.ROLLBACK;

        // 5.判断订单支付状态
        Integer status = orders.getStatus();

        Integer paidStatus = new Integer(OrderDealStatusEnum.ORDER_DEAL_PAID_STATUS.getCode());

        // 如果订单已经支付，事务进行提交
        if (paidStatus.equals(status))
            return RocketMQLocalTransactionState.COMMIT;

        // 如果订单没有支付成功，事务回滚
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}