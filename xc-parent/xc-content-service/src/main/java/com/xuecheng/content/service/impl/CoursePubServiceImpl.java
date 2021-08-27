package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.common.enums.content.CourseModeEnum;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.JsonUtil;
import com.xuecheng.content.common.constant.ContentErrorCode;
import com.xuecheng.content.common.util.QiniuUtils;
import com.xuecheng.content.entity.CourseMarket;
import com.xuecheng.content.entity.CoursePub;
import com.xuecheng.content.mapper.CoursePubMapper;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.CoursePubService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 课程发布 服务实现类
 * </p>
 */
@Slf4j
@Service
public class CoursePubServiceImpl extends ServiceImpl<CoursePubMapper, CoursePub> implements CoursePubService {

}