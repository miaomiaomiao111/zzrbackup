package com.xuecheng.content.convert;


import com.xuecheng.api.content.model.dto.CoursePubDTO;
import com.xuecheng.content.entity.CourseBase;
import com.xuecheng.content.entity.CoursePub;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CoursePubConvert {

    CoursePubConvert INSTANCE = Mappers.getMapper(CoursePubConvert.class);

    CoursePub courseBase2coursePub(CourseBase courseBase);


    CoursePubDTO entity2dto(CoursePub coursePub);

}