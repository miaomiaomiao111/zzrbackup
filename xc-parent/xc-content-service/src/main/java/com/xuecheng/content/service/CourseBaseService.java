package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.content.entity.CourseBase;
import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.content.model.qo.QueryCourseModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author itcast
 * @since 2021-08-03
 */
@Service
public interface CourseBaseService extends IService<CourseBase> {
    List<CourseBase> queryAll();

    /**
     * 课程基础信息条件查询
     *
     * @param params 分页数据
     * @param model  查询条件数据
     * @return
     */
    PageVO queryCourseList(PageRequestParams params, QueryCourseModel model,Long companyId);

    /**
     *
     * @param courseBaseDTO 课程基础信息数据
     * @return  CourseBaseDTO
     */
    CourseBaseDTO createCourseBase(CourseBaseDTO courseBaseDTO);


    /**
     * 根据Id查询课程基础信息
     * @param courseBaseId 课程的Id值
     * @param companyId 公司的Id值
     * @return CourseBaseDTO
     */
    CourseBaseDTO getCourseBase(Long courseBaseId,Long companyId);


    /**
     * 修改课程基础信息
     * @param dto CourseBaseDTO 课程基础信息数据
     * @return
     */
    CourseBaseDTO modifyCourseBase(CourseBaseDTO dto);

    /**
     *根据Id删除课程基础信息
     * @param courseBaseId 课程的Id值
     * @param companyId 公司的Id值
     */
    void removeCourseBase(Long courseBaseId,Long companyId);

    /**
     * 提交课程
     * @param courseBaseId 课程的ID值
     * @param companyId  公司的ID值
     */
    void commitCourseBase(Long courseBaseId, Long companyId);

    /**
     * 课程审核
     * @param dto -课程基础信息数据
     */
    void approve(CourseBaseDTO dto);
    /**
     * 课程预览
     * @param courseId 课程Id
     * @param companyId 公司Id
     * @return Map 数据模型 Map
     */
    Map<String, Object> preview(Long courseBaseId, Long companId);

    /**
     * 课程发布
     * @param courseId
     * @param companyId
     * @return
     */
    void publish(Long courseBaseId, Long companyId);


    /**
     * 执行本地事务保存CoursePub并更改课程审核状态
     * @return
     */
    void excuteProducerLocalData(Long courseBaseId);


    void publishPage(Long courseBaseId);
}
