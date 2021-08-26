package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.api.content.model.dto.TeachplanMediaDTO;
import com.xuecheng.content.entity.Teachplan;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 * @author itcast
 * @since 2021-08-07
 */
public interface TeachplanService extends IService<Teachplan> {


    /**
     * 根据课程Id查询课程计划（树形结构）
     * @param courseId 课程Id
     * @param companyId  公司Id
     * @return
     */
    TeachplanDTO queryTreeNodesByCourseId(Long courseId, Long companyId);

    /**
     * 创建或修改课程计划信息
     * @param teachplanDTO  课程计划信息
     * @return TeachplanDTO 课程计划信息
     */
    TeachplanDTO createOrModifyTeachPlan(TeachplanDTO teachplanDTO,Long companyId);

    /**
     * 课程计划绑定媒资
     * @param teachplanMediaDTO
     * @param companyId
     * @return
     */
    TeachplanMediaDTO associateMedia(TeachplanMediaDTO teachplanMediaDTO, Long companyId);
}
