package com.xuecheng.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.xuecheng.agent.order.ContentSearchApiAgent;
import com.xuecheng.api.order.model.dto.OrdersDTO;
import com.xuecheng.api.order.model.pay.PayCodeUrlResult;
import com.xuecheng.api.order.model.pay.PayResultModel;
import com.xuecheng.api.search.model.dto.CoursePubIndexDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.enums.common.CommonEnum;
import com.xuecheng.common.enums.order.OrderDealStatusEnum;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.JsonUtil;
import com.xuecheng.common.util.PaymentUtil;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.order.common.constant.OrderErrorCode;
import com.xuecheng.order.convert.OrderConvert;
import com.xuecheng.order.entity.Orders;
import com.xuecheng.order.entity.Pay;
import com.xuecheng.order.mapper.OrdersMapper;
import com.xuecheng.order.service.OrdersService;
import com.xuecheng.order.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ContentSearchApiAgent searchApiAgent;


    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private PayService payService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    @Transactional
    public OrdersDTO createOrder(Long coursePubId, String username) {
        //1.判断关键数据
        //    coursePubId username
        if (ObjectUtils.isEmpty(coursePubId) ||
                StringUtil.isBlank(username)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        // 2.判断业务数据
        //     用户数据（UAA已经校验）
        //     课程数据
        //         判断课程数据是否存在--发送feign接口获得数据
        RestResponse<CoursePubIndexDTO> restResponse = searchApiAgent.getCoursePubIndexById4s(coursePubId);

        if (!(restResponse.isSuccessful())) {
            ExceptionCast.cast(OrderErrorCode.E_160002);
        }

        CoursePubIndexDTO coursePubIndexDTO = restResponse.getResult();
        // 3.保存用户的订单数据
        // 判断订单数据是否存在：coursePubId  username
        //如果不存在  创建订单数据并保存
        //如果存在     判断订单状态：必须为初始态
        //             更新订单的数据：价格
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Orders::getUserName, username);
        queryWrapper.eq(Orders::getCoursePubId, coursePubId);

        Orders orders = this.getOne(queryWrapper);
        if (ObjectUtils.isEmpty(orders)) {
            //没有 创建订单数据并保存
            orders = new Orders();

            // 订单编号：唯一  日期 体现系统的标识
            String orderNo = PaymentUtil.genUniquePayOrderNo();
            orders.setOrderNo(orderNo);
            orders.setCoursePubId(coursePubId);
            orders.setCoursePubName(coursePubIndexDTO.getName());
            orders.setCompanyId(coursePubIndexDTO.getCompanyId());

            orders.setUserName(username);


            Float price = coursePubIndexDTO.getPrice();
            orders.setInitialPrice(price);
            orders.setPrice(price);

            orders.setStatus(new Integer(OrderDealStatusEnum.ORDER_DEAL_INIT_STATUS.getCode()));

            boolean result = this.save(orders);

            ExceptionCast.cast(!result, OrderErrorCode.E_160001);
        } else {
            Integer status = orders.getStatus();
            if (!(OrderDealStatusEnum.ORDER_DEAL_INIT_STATUS.getCode().equals(status.toString()))) {
                ExceptionCast.cast(OrderErrorCode.E_160014);
            }
            //更新订单
            LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Orders::getPrice, coursePubIndexDTO.getPrice());
            updateWrapper.set(Orders::getChangeDate, LocalDateTime.now());
            updateWrapper.eq(Orders::getId, orders.getId());

            boolean result = this.update(updateWrapper);

            ExceptionCast.cast(!result, OrderErrorCode.E_160015);
        }
        // 4.返回结果数据DTO
        OrdersDTO dto = OrderConvert.INSTANCE.entity2dto(orders);

        return dto;
    }

    @Override
    @Transactional
    /*
     * 生成支付得url
     * */
    public PayCodeUrlResult createPayCodeResult(String orderNo, String username) {
        //1.判断关键数据
        //   orderNo  username
        if (StringUtil.isBlank(orderNo) ||
                StringUtil.isBlank(username)
        ) {
            return PayCodeUrlResult.failed(CommonErrorCode.E_100101.getDesc());
        }
        // 2.判断业务数据
        LambdaQueryWrapper<Orders> orderQueryWrapper = new LambdaQueryWrapper<>();

        orderQueryWrapper.eq(Orders::getOrderNo, orderNo);
        orderQueryWrapper.eq(Orders::getUserName, username);

        Orders orders = this.getOne(orderQueryWrapper);
        // 判断订单数据是否存在  判断订单用户是否存在
        if (ObjectUtils.isEmpty(orders)) {
            return PayCodeUrlResult.failed(OrderErrorCode.E_160008.getDesc());
        }
        // 判断订单的状态：只能是初始态才能支付
        Integer status = orders.getStatus();
        if (!(OrderDealStatusEnum.ORDER_DEAL_INIT_STATUS.getCode().equals(status.toString()))) {
            return PayCodeUrlResult.failed(OrderErrorCode.E_160014.getDesc());
        }
        //3.和第三方支付系统交互生成支付地址
        String codeURL = null;
        try {
            WxPayUnifiedOrderRequest unifiedOrderRequest = new WxPayUnifiedOrderRequest();
            unifiedOrderRequest.setBody("课程的信息：" + orders.getCoursePubName());
            unifiedOrderRequest.setOutTradeNo(orders.getOrderNo());
            unifiedOrderRequest.setSpbillCreateIp(InetAddress.getLocalHost().getHostAddress());
            Float price = orders.getPrice();
            unifiedOrderRequest.setTotalFee((int) (price * 100));
            unifiedOrderRequest.setProductId("课程的id：" + orders.getCoursePubId());

            // 获得响应数据并解析获得支付连接地址
            WxPayUnifiedOrderResult wxResponse = wxPayService.unifiedOrder(unifiedOrderRequest);

            String returnCode = wxResponse.getReturnCode();
            String resultCode = wxResponse.getResultCode();

            if (!(PayCodeUrlResult.WX_PAY_SUCCESS_FLAG.equals(returnCode)) ||
                    !(PayCodeUrlResult.WX_PAY_SUCCESS_FLAG.equals(resultCode))
            ) {
                return PayCodeUrlResult.failed(OrderErrorCode.E_160016.getDesc());
            }

            codeURL = wxResponse.getCodeURL();
        } catch (Exception e) {
            log.error("第三方支付生成支付连接失败：{}", e.getMessage());
            return PayCodeUrlResult.failed(OrderErrorCode.E_160016.getDesc());
        }

        // 4.生成订单支付数据-OrderPay
        //     判断订单支付数据是否存在：orderId
        //         如果有：
        //             不做操作
        //         如果没有：
        //             创建订单支付数据
        LambdaQueryWrapper<Pay> payQueryWrapper = new LambdaQueryWrapper<>();
        payQueryWrapper.eq(Pay::getOrderId, orders.getId());

        Pay pay = payService.getOne(payQueryWrapper);
        if (ObjectUtils.isEmpty(pay)) {

            pay = new Pay();

            pay.setUserName(username);
            pay.setCompanyId(orders.getCompanyId());
            pay.setOrderId(orders.getId());
            pay.setStatus(CommonEnum.DELETE_FLAG.getCode());
            pay.setPayMethod(PayCodeUrlResult.WX_PAY_FLAG);

            boolean payResult = payService.save(pay);

            if (!payResult) {
                return PayCodeUrlResult.failed(OrderErrorCode.E_160009.getDesc());
            }
        } else {
            // 判断订单支付数据的状态
            String payStatus = pay.getStatus();
            if (!(CommonEnum.DELETE_FLAG.getCode().equals(payStatus))) {
                return PayCodeUrlResult.failed(OrderErrorCode.E_160014.getDesc());
            }

        }
        return PayCodeUrlResult.success(codeURL);
    }


    /*order.pay.topic = topic_order_pay*/
    @Value("${order.pay.topic}")
    private String payTopic;

    /*
     * 业务分析
     *   1.解析支付结果
     *   获得支付成功的结果:return_code 和 result_code 必须为SUccESs
     *
     *   2.判断业务数据
     *       2.1订单数据
     *           判断订单是否存在:订单编号(商户的编号）
     *           订单支付状态 必须为初始态
     *           判断支付的签名（无需判断，微信的封装sdk已经判断）
     *           判断订单的金额
     *       2.2 订单支付数据：是否存在，交易状态

     *   3.发送事务消息
     *          从支付结果中获得内容并封装:PayResultNodel
     *          将支付后的结果封装数据发送PayResultNodel
     * */
    @Override
    public void payNotifyResult(String xmlResult) {
        //1.解析支付结果
        //获得支付成功的结果:return_code 和 result_code 必须为SUccESs
        WxPayOrderNotifyResult result = null;
        try {
            result = wxPayService.parseOrderNotifyResult(xmlResult);
        } catch (WxPayException e) {
            log.error(OrderErrorCode.E_160019.getDesc() + ": error {}", e.getMessage());
            ExceptionCast.cast(OrderErrorCode.E_160019);

        }

        String resultCode = result.getResultCode();
        String returnCode = result.getReturnCode();
        if (!(PayCodeUrlResult.WX_PAY_SUCCESS_FLAG.equals(returnCode)) ||
                !(PayCodeUrlResult.WX_PAY_SUCCESS_FLAG.equals(resultCode))) {
            String errCodeDes = result.getErrCodeDes();
            ExceptionCast.castWithExceptionMsg(OrderErrorCode.E_160010, errCodeDes);
        }
        /*
         *      2.判断业务数据
         *       2.1订单数据
         *           判断订单是否存在:订单编号(商户的编号）
         *           订单支付状态 必须为初始态
         *           判断支付的签名（无需判断，微信的封装sdk已经判断）
         *           判断订单的金额
         *       2.2 订单支付数据：是否存在，交易状态*/
        //判断订单是否存在:订单编号(商户的编号）
        String orderNo = result.getOutTradeNo();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getOrderNo, orderNo);
        Orders orders = getOne(queryWrapper);
        ExceptionCast.cast(ObjectUtils.isEmpty(orders), OrderErrorCode.E_160008);
        //订单支付状态 必须为初始态
        Integer status = orders.getStatus();
        ExceptionCast.cast(!(OrderDealStatusEnum.ORDER_DEAL_INIT_STATUS.getCode().equals(status.toString())),
                OrderErrorCode.E_160014);
        //判断订单的金额
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());
        Float price = orders.getPrice();
        if (!ObjectUtils.nullSafeEquals(totalFee, price.toString())) {
            ExceptionCast.cast(OrderErrorCode.E_160017);
        }
        //订单支付数据：是否存在，交易状态
        LambdaQueryWrapper<Pay> queryWrapperPay = new LambdaQueryWrapper<>();
        queryWrapperPay.eq(Pay::getOrderId, orders.getId());
        Pay pay = payService.getOne(queryWrapperPay);
        ExceptionCast.cast(ObjectUtils.isEmpty(pay), OrderErrorCode.E_160018);
        //交易状态
        String payStatus = pay.getStatus();
        ExceptionCast.cast(!CommonEnum.DELETE_FLAG.getCode().equals(payStatus), OrderErrorCode.E_160014);

        /*
         * 3.发送事务消息
         *       从支付结果中获得内容并封装:PayResultNodel
         *       将支付后的结果封装数据发送PayResultNodel
         * */
        PayResultModel payResultModel = new PayResultModel();
        OrdersDTO ordersDTO = OrderConvert.INSTANCE.entity2dto(orders);
        payResultModel.setOrder(ordersDTO);
        //用户通过什么方式支付（扫码，扫码枪）
        String tradeType = result.getTradeType();
        payResultModel.setPayMethod(tradeType);
        //支付时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeEnd = result.getTimeEnd();
        LocalDateTime time = LocalDateTime.parse(timeEnd, formatter);
        payResultModel.setPayDate(time);

        // 第三方支付系统的订单号(这里指微信)
        String transactionId = result.getTransactionId();
        payResultModel.setPayNumber(transactionId);

        // 订单总金额
        payResultModel.setTotalAmount(new BigDecimal(totalFee.toString()));

        // 应付的金额
        Integer settlementTotalFee = result.getSettlementTotalFee();
        if (!(ObjectUtils.isEmpty(settlementTotalFee))) {
            payResultModel.setReceiptAmount(new BigDecimal(settlementTotalFee.toString()));
        }


        // 用户实际支付金额
        Integer cashFee = result.getCashFee();
        if (!(ObjectUtils.isEmpty(cashFee))) {
            payResultModel.setBuyerPayAmount(new BigDecimal(cashFee.toString()));
        }

        //微信通知内容的xml数据
        String xmlString = result.getXmlString();
        payResultModel.setPayResponse(xmlString);
        //交易状态
        payResultModel.setTradeStatus(resultCode);
        String payJson = JsonUtil.objectTojson(payResultModel);
        //将支付后的结果封装数据发送PayResultNodel
        Message<String> message = MessageBuilder.withPayload(payJson).build();

        try {
            TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(payTopic, message, null);
            if (LocalTransactionState.COMMIT_MESSAGE != sendResult.getLocalTransactionState()) {
                log.error("发送事务消息失败，{}", sendResult.getLocalTransactionState());
                ExceptionCast.cast(OrderErrorCode.E_160011);
            }
        } catch (MessagingException e) {
            log.error("发送事务消息失败，{}", e.getMessage());
            ExceptionCast.cast(OrderErrorCode.E_160011);


        }


    }

    @Override
    @Transactional
    public void successPayment(PayResultModel payResultModel) {
        //1.判断业务数据
        //orders（不需要判断-PayResultModel中已经存在了一个没有问题的数据）
        //orderpay（不需要判断，发送事务消息都已经做了业务的判断）
        //更改订单相关的数据
        //   orders——>orderStatus
        // 更改orders的交易状态
        OrdersDTO order = payResultModel.getOrder();
        LambdaUpdateWrapper<Orders> orderUpdateWrapper = new LambdaUpdateWrapper<>();
        orderUpdateWrapper.set(Orders::getStatus, new Integer(OrderDealStatusEnum.ORDER_DEAL_PAID_STATUS.getCode()));
        orderUpdateWrapper.set(Orders::getChangeDate, LocalDateTime.now());
        orderUpdateWrapper.eq(Orders::getId, order.getOrderId());

        boolean orderResult = this.update(orderUpdateWrapper);

        ExceptionCast.cast(!orderResult, OrderErrorCode.E_160012);

        //   order_pay
        //       支付状态
        //       支付的金额
        //       支付的时间
        //       第三方支付订单号
        //       第三方支付的通知数据
        LambdaQueryWrapper<Pay> payQueryWrapper = new LambdaQueryWrapper<>();
        payQueryWrapper.eq(Pay::getOrderId, order.getOrderId());

        Pay pay = payService.getOne(payQueryWrapper);

        String payNumber = payResultModel.getPayNumber();
        pay.setPayNumber(payNumber);

        pay.setStatus(CommonEnum.USING_FLAG.getCode());
        pay.setPayMethod(payResultModel.getPayMethod());
        pay.setPayDate(payResultModel.getPayDate());


        pay.setTotalAmount(payResultModel.getTotalAmount());
        pay.setReceiptAmount(payResultModel.getReceiptAmount());
        pay.setBuyerPayAmount(payResultModel.getBuyerPayAmount());

        pay.setPayResponse(payResultModel.getPayResponse());


        boolean payResult = payService.updateById(pay);

        ExceptionCast.cast(!payResult,OrderErrorCode.E_160013);

    }
}