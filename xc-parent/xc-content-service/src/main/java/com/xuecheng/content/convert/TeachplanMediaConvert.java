package com.xuecheng.content.convert;


import com.xuecheng.api.content.model.dto.TeachplanMediaDTO;
import com.xuecheng.content.entity.TeachplanMedia;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * 课程计划媒资的PO和DTO转换器
 */
@Mapper
public interface TeachplanMediaConvert {

    TeachplanMediaConvert INSTANCE = Mappers.getMapper(TeachplanMediaConvert.class);

    TeachplanMedia dto2entity(TeachplanMediaDTO teachplanDTO);

    TeachplanMediaDTO entity2dto(TeachplanMedia teachplanMedia);

}