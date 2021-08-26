package com.xuecheng.content.listener;

import com.xuecheng.content.service.CourseBaseService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "${course.publish.topic}",consumerGroup = "consumer_group_course_pub")
public class CoursePublishConsumerListener implements RocketMQListener<Long> {

    @Autowired
    private CourseBaseService courseBaseService;

    @Override
    public void onMessage(Long courseBaseId)  {
        try {
            courseBaseService.publishPage(courseBaseId);
        } catch (Exception e) {
            //异常会存在丢失
            throw new RuntimeException("课程发布消费方执行失败",e);
        }
    }
}