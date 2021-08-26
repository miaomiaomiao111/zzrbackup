package com.xuecheng.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.api.system.model.dto.CourseCategoryDTO;
import com.xuecheng.system.entity.CourseCategory;
import com.xuecheng.system.entity.ex.CourseCategoryNode;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    /**
     * 查询课程分类树形结构数据
     * @return
     */
    List<CourseCategoryNode> selectTreenNodes();
}
