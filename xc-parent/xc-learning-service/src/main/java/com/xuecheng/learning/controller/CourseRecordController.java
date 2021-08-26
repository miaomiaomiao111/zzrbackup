package com.xuecheng.learning.controller;

import com.xuecheng.api.learning.model.dto.CourseRecordDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.uaa.LoginUser;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.learning.common.utils.UAASecurityUtil;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xuecheng.learning.service.CourseRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 选课记录 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class CourseRecordController {

    @Autowired
    private CourseRecordService  courseRecordService;




    @GetMapping("learnedRecords/myCourseRec/{coursePubId}")
    public CourseRecordDTO getRecordByCoursePubId(@PathVariable Long coursePubId) {

        LoginUser user = UAASecurityUtil.getUser();
        ExceptionCast.cast(ObjectUtils.isEmpty(user), CommonErrorCode.E_403000);

        String username = user.getUsername();

        CourseRecordDTO dto = courseRecordService.getMyCourseRecord(username, coursePubId);

        return dto;
    }
}
