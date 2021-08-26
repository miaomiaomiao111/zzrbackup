package com.xuecheng.learning.convert;


import com.xuecheng.api.learning.model.dto.CourseRecordDTO;
import com.xuecheng.learning.entity.CourseRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CourseRecordConvert {

	CourseRecordConvert INSTANCE = Mappers.getMapper(CourseRecordConvert.class);

	@Mappings({
			@Mapping(source = "id", target = "courseRecordId"),
	})
	CourseRecordDTO entity2dto(CourseRecord entity);

	@Mapping(source = "courseRecordId", target = "id")
    CourseRecord dto2entity(CourseRecordDTO dto);

	List<CourseRecordDTO> entitys2dtos(List<CourseRecord> list);

}