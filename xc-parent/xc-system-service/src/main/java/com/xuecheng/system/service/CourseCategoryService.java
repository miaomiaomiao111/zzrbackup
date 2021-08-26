package com.xuecheng.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.system.model.dto.CourseCategoryDTO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.system.entity.CourseCategory;

import java.util.List;

/**
 * <p>
 * 课程分类 服务类
 * </p>
 *
 * @author itcast
 * @since 2021-08-06
 */
public interface CourseCategoryService extends IService<CourseCategory> {
    /**
     * 返回课程分类树形数据
     * @return List<CourseCategoryNode>
     */
    List<CourseCategoryDTO> queryTreeNodes();

    RestResponse<CourseCategoryDTO> getById4s(String courseCategoryId);
}
