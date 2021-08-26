package com.xuecheng.media.convert;

import com.xuecheng.api.media.model.dto.MediaAuditDTO;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.vo.MediaAuditVO;
import com.xuecheng.api.media.model.vo.MediaVO;
import com.xuecheng.media.entity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MediaAuditConvert {

    MediaAuditConvert INSTANCE = Mappers.getMapper(MediaAuditConvert.class);

    MediaAuditDTO vo2dto(MediaAuditVO mediaAuditVO);

}