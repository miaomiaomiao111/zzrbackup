package com.xuecheng.media.controller;

import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.media.MediaAuditApi;
import com.xuecheng.api.media.model.dto.MediaAuditDTO;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.vo.MediaAuditVO;
import com.xuecheng.media.convert.MediaAuditConvert;
import com.xuecheng.media.convert.MediaConvert;
import com.xuecheng.media.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MediaAuditController implements MediaAuditApi {

    @Autowired
    private MediaService mediaService;

    @PutMapping("m/media/audit")
    public void auditMedia(@RequestBody MediaAuditVO auditVO) {

        MediaAuditDTO mediaDTO = MediaAuditConvert.INSTANCE.vo2dto(auditVO);
        //调用service方法进行课程审核
        mediaService.auditMedia(mediaDTO);
    }
}
