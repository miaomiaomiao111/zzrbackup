package com.xuecheng.teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.teaching.entity.Work;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 作业 Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2019-09-18
 */
@Repository
public interface WorkMapper extends BaseMapper<Work> {

    @Select("select * from work where id = #{workId}")
    Work getByIdAndCoursePubId(@Param("workId") Long workId);
}
