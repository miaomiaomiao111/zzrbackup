package com.xuecheng.learning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.learning.entity.CourseRecord;
import com.xuecheng.learning.mapper.CourseRecordMapper;
import com.xuecheng.learning.service.CourseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 选课记录 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseRecordServiceImpl extends ServiceImpl<CourseRecordMapper, CourseRecord> implements CourseRecordService {

}
