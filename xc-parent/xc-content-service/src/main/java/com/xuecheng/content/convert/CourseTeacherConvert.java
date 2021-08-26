package com.xuecheng.content.convert;

import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.api.content.model.vo.CourseTeacherVO;
import com.xuecheng.content.entity.CourseTeacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CourseTeacherConvert {

    CourseTeacherConvert INSTANCE = Mappers.getMapper(CourseTeacherConvert.class);

    @Mapping(source = "id",target = "courseTeacherId")
    CourseTeacherDTO entity2dto(CourseTeacher courseTeacher);

    List<CourseTeacherDTO> entity2dtos(List<CourseTeacher> courseTeacherList);

    CourseTeacherDTO vo2dto(CourseTeacherVO courseTeacherVO);

    @Mapping(source = "courseTeacherId",target = "id")
    CourseTeacher dto2entity(CourseTeacherDTO courseTeacherDTO);

    List<CourseTeacher> dto2entitys(List<CourseTeacherDTO> courseTeacherDTOList);
}
