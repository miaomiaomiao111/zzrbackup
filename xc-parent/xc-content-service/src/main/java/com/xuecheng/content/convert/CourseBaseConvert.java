package com.xuecheng.content.convert;

import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.vo.CourseAuditVO;
import com.xuecheng.api.content.model.vo.CourseBaseVO;
import com.xuecheng.content.entity.CourseBase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 对象属性转换器：
 * PS：将MapStruct依赖添加到工程中
 * 1.创建一个接口并在类上添加 @Mapper 注解
 * 2.在接口通过MapStruct的api创建出接口的实例对象
 * 3.转换方法
 * 传入参数、传出参数
 * 将传入参数里的数据会赋值给传出参数
 * <p>
 * 特点：
 * 1.对象中的属性转换数据时，默认情况下将两个对象中的同属性名进行赋值操作
 *
 * </p>
 *
 * @Description:
 */
@Mapper
public interface CourseBaseConvert {

    CourseBaseConvert INSTANCE = Mappers.getMapper(CourseBaseConvert.class);

    // 将po转为dto数据
    @Mapping(source = "id", target = "courseBaseId")
    CourseBaseDTO entity2dto(CourseBase courseBase);


    // 将pos转为dtos数据
    /*
     * 集合的方法会依赖于单个数据转换的方法
     *       entitys2dtos-》entity2dto
     *       Mapping注解是使用在单个数据转换方法上的，不是在集合方法上来使用
     * */
    List<CourseBaseDTO> entitys2dtos(List<CourseBase> courseBase);

    CourseBaseDTO vo2dto(CourseBaseVO courseBaseVO);

    @Mapping(source = "courseBaseId", target = "id")
    CourseBase dto2entity(CourseBaseDTO dto);


    CourseBaseDTO vo2dto(CourseAuditVO courseAuditVO);


}