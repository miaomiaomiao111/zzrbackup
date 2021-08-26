package com.xuecheng.media.convert;

import com.xuecheng.api.media.model.dto.MediaAuditDTO;
import com.xuecheng.api.media.model.vo.MediaAuditVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MediaAuditConvert {

    MediaAuditConvert INSTANCE = Mappers.getMapper(MediaAuditConvert.class);

    MediaAuditDTO vo2dto(MediaAuditVO mediaAuditVO);

}