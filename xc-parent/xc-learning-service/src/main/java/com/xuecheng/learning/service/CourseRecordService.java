package com.xuecheng.learning.service;

import com.xuecheng.api.learning.model.dto.CourseRecordDTO;
import com.xuecheng.learning.entity.CourseRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 选课记录 服务类
 * </p>
 *
 * @author itcast
 * @since 2021-08-23
 */
public interface CourseRecordService extends IService<CourseRecord> {
    /**
     * 获取用户的课程 学习进度
     * @param username 用户名
     * @param coursePubId 课程发布id
     * @return
     */
    CourseRecordDTO getMyCourseRecord(String username, Long coursePubId);
    /**
     * 创建用户的学习记录数据
     * @param courseRecordDTO
     */
    void createOrModifyCourseRecord(CourseRecordDTO courseRecordDTO);
}
