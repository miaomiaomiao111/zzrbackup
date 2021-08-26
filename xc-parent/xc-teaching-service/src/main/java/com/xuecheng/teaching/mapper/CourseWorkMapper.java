package com.xuecheng.teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.teaching.entity.CourseWork;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2019-11-26
 */
@Repository
public interface CourseWorkMapper extends BaseMapper<CourseWork> {

    @Select("select * from course_work where company_id = #{companyId} and course_pub_id = #{coursePubId} ")
    CourseWork getCourseWorkByCourse(@Param("companyId") Long companyId, @Param("coursePubId") Long coursePubId);


}
