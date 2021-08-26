package com.xuecheng.search.convert;

import com.xuecheng.api.search.model.dto.CoursePubIndexDTO;
import com.xuecheng.search.model.CoursePubIndex;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 课程发布索引PO DTO转换
 */
 @Mapper
 public interface CoursePubIndexConvert {

     CoursePubIndexConvert INSTANCE = Mappers.getMapper(CoursePubIndexConvert.class);

     @Mapping(source = "id",target = "indexId")
     CoursePubIndexDTO index2dto(CoursePubIndex coursePubIndex);


     @Mapping(source = "indexId",target = "id")
     CoursePubIndex dto2index(CoursePubIndexDTO coursePubIndexDTO);


     List<CoursePubIndexDTO> indexs2dtos(List<CoursePubIndex> coursePubIndices);

 }
