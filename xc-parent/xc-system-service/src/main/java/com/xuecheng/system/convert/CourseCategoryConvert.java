package com.xuecheng.system.convert;

import com.xuecheng.api.system.model.dto.CourseCategoryDTO;
import com.xuecheng.system.entity.CourseCategory;
import com.xuecheng.system.entity.ex.CourseCategoryNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CourseCategoryConvert {

    // 构建转换器示例
    CourseCategoryConvert INSTANCE = Mappers.getMapper(CourseCategoryConvert.class);

    /**
     * 单个数据转为 DTO数据
     * @param categoryNode
     * @return
     */
    @Mappings({
            @Mapping(source = "id", target = "courseCategoryId"),
            @Mapping(source = "childrenTreeNodes", target = "categoryTreeNodes")
    })
    CourseCategoryDTO node2dto(CourseCategoryNode categoryNode);

    /**
     * 集合数据转为 DTO数据
     * @param categoryNode
     * @return
     */
    List<CourseCategoryDTO> nodes2dtos(List<CourseCategoryNode> categoryNode);



    @Mapping(source = "id", target = "courseCategoryId")
    CourseCategoryDTO entity2dto(CourseCategory courseCategory);
}