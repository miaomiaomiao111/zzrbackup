package com.xuecheng.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.system.model.dto.CourseCategoryDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.system.common.constant.SystemErrorCode;
import com.xuecheng.system.convert.CourseCategoryConvert;
import com.xuecheng.system.entity.CourseCategory;
import com.xuecheng.system.entity.ex.CourseCategoryNode;
import com.xuecheng.system.mapper.CourseCategoryMapper;
import com.xuecheng.system.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {

    @Override
    public List<CourseCategoryDTO> queryTreeNodes() {

        // 1.获得mapper中的自定义方法
        // PS：mp对于service获得dao层的mapper对象，提供了对应方法
        CourseCategoryMapper baseMapper = this.getBaseMapper();


        // 2.获得树形结构数据Node
        List<CourseCategoryNode> nodes = baseMapper.selectTreenNodes();


        // 3.将node转为dto数据
        List<CourseCategoryDTO> dtos = CourseCategoryConvert.INSTANCE.nodes2dtos(nodes);

        return dtos;
    }

    @Override
    public RestResponse<CourseCategoryDTO> getById4s(String courseCategoryId) {
        // 1.判断关键数据
        if (StringUtil.isBlank(courseCategoryId)) {
            return RestResponse.validfail(CommonErrorCode.E_100101);
        }

        // 2.根据id查询数据
        CourseCategory courseCategory = this.getById(courseCategoryId);

        // 3.判断结果数据返回对应的内容
        if (ObjectUtils.isEmpty(courseCategory)) {
            return RestResponse.validfail(SystemErrorCode.E_110100);
        } else {
            CourseCategoryDTO dto = CourseCategoryConvert.INSTANCE.entity2dto(courseCategory);
            return RestResponse.success(dto);
        }

    }
}
