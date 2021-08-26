package com.xuecheng.learning.listener;

import com.alibaba.fastjson.JSON;
import com.xuecheng.api.learning.model.dto.CourseRecordDTO;
import com.xuecheng.api.order.model.dto.OrdersDTO;
import com.xuecheng.api.order.model.pay.PayResultModel;
import com.xuecheng.learning.service.CourseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 *
 * @Description:
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "consumer_group_course_record",topic = "${order.pay.topic}")
public class CourseRecordListener implements RocketMQListener<String> {

    @Autowired
    private CourseRecordService courseRecordService;


    @Override
    public void onMessage(String jsonString) {

        // 1.解析json数据
        PayResultModel payResultModel = JSON.parseObject(jsonString, PayResultModel.class);

        // 获得课程学习记录的数据（课程的发布Id、用户的名称）
        OrdersDTO order = payResultModel.getOrder();

        CourseRecordDTO courseRecordDTO = new CourseRecordDTO();

        courseRecordDTO.setCoursePubId(order.getCoursePubId());
        courseRecordDTO.setUserName(order.getUserName());


        // 2.调用学习记录的业务类方法
        courseRecordService.createOrModifyCourseRecord(courseRecordDTO);

    }
}