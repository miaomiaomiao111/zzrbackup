package com.xuecheng.content.controller;

import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.util.SecurityUtil;
import com.xuecheng.content.entity.TeachplanMedia;
import com.xuecheng.content.service.TeachplanMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
@RequestMapping("teachplanMedia")
public class TeachplanMediaController {

    @Autowired
    private TeachplanMediaService  teachplanMediaService;
    @GetMapping("/{mediaId}")
    public RestResponse<Boolean> verifyTreeNodeByCourseId(@PathVariable("mediaId") Long mediaId) {



        RestResponse<Boolean> response = teachplanMediaService.queryTeachplanMediaByMediaId(mediaId);
        return response;

    }
}
