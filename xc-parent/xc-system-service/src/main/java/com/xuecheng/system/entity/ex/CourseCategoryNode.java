package com.xuecheng.system.entity.ex;

import com.xuecheng.system.entity.CourseCategory;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 		课程分类信息的子节点，扩展课程分类 PO 类，增加子节点集合属性
 * </p>
 *
 * @Description:
 */
@Data
public class CourseCategoryNode extends CourseCategory {


    /**
     * 子节点数据集合，课程分类为3级分类
     * 为方便树形结构数据的返回，定义子节点的集合属性
     */
    List<CourseCategoryNode> childrenTreeNodes;
}