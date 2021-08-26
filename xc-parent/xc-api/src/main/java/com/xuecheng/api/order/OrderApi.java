package com.xuecheng.api.order;

import com.xuecheng.api.order.model.dto.OrdersDTO;
import com.xuecheng.api.order.model.pay.PayCodeUrlResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p></p>
 *
 * @Description:
 */
@Api("订单服务api")
public interface OrderApi {


    @ApiOperation("创建用户订单")
    OrdersDTO createOrder(Long coursePubId);

    @ApiOperation("生成支付的地址")
    PayCodeUrlResult createPayCodeResult(String orderNo);
}
