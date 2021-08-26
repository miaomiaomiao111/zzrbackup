package com.xuecheng.content.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.common.enums.content.CourseAuditEnum;
import com.xuecheng.content.entity.CourseBase;
import com.xuecheng.content.entity.CoursePub;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.CoursePubService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @Author: ccw
 * @Description: TODO
 * @DateTime: 2021/8/18 10:52
 **/
@Slf4j
@Component
@RocketMQTransactionListener
public class CoursePublishTxMsgListener implements RocketMQLocalTransactionListener {

    @Autowired
    private CoursePubService coursePubService;

    @Autowired
    private CourseBaseService courseBaseService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {

            byte[] bytes = (byte[]) message.getPayload();

            String courseId = new String(bytes);

            courseBaseService.excuteProducerLocalData(Long.parseLong(courseId));
            // 3.返回执行本地事务的结果
            return RocketMQLocalTransactionState.COMMIT;

        }catch (NumberFormatException e) {

            log.error("课程发布消息生产方执行本地事务失败：{}",e.getMessage());

            // 3.返回执行本地事务的结果
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /*
     * 业务分析：
     *   1.获得消息并解析
     *       courseBaseId
     *   2.根据课程基础信息id获得课程基本信息数据并校验课程的审核状态
     *   3.根据课程基础信息id获得课程发布数据，判断数据是否存在
     * */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        byte[] bytes = (byte[]) message.getPayload();

        Long courseId = Long.parseLong(new String(bytes));
        // 2.根据课程基础信息id获得课程发布数据，判断数据是否存在
        LambdaQueryWrapper<CoursePub> pubQueryWrapper = new LambdaQueryWrapper<>();
        pubQueryWrapper.eq(CoursePub::getCourseId, courseId);

        int coursePubCount = coursePubService.count(pubQueryWrapper);

        if (coursePubCount != 1) {
            log.error("课程发布后获得课程发布信息数据失败：{}",courseId);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        //查询课程发布信息
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(CourseBase::getId,courseId);

        queryWrapper.eq(CourseBase::getAuditStatus, CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode());

        int courseBaseCount = courseBaseService.count(queryWrapper);

        // 如果数据错误，通知MQ生产方执行本地事务失败
        if (courseBaseCount != 1) {
            log.error("课程发布后获得课程基础信息数据失败：{}",courseId);
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        return RocketMQLocalTransactionState.COMMIT;
    }
}
