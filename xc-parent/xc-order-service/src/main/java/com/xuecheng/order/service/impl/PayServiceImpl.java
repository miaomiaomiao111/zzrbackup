package com.xuecheng.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.order.entity.Pay;
import com.xuecheng.order.mapper.PayMapper;
import com.xuecheng.order.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单支付信息 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements PayService {

}