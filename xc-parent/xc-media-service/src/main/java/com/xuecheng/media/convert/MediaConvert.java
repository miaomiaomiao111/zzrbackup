package com.xuecheng.media.convert;

import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.vo.MediaVO;
import com.xuecheng.media.entity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 媒资信息转换类
 */
@Mapper
public interface MediaConvert {

    MediaConvert INSTANCE = Mappers.getMapper(MediaConvert.class);

    MediaDTO entity2dto(Media media);

    List<MediaDTO> entity2dtos(List<Media> list);

    Media dto2entity(MediaDTO media);

    MediaDTO vo2dto(MediaVO vo);
}