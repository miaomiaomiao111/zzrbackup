package com.xuecheng.teaching.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 * @since 2019-11-26
 */
@Data
@TableName("course_work_rec")
public class CourseWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程发布ID
     */
    private Long coursePubId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 机构ID
     */
    private Long companyId;

    /**
     * 机构名称
     */
    private String companyName;

    /**
     * 课程包含的作业总数
     */
    private Integer workNumber;

    /**
     * 答题总数[所有提交的作业]
     */
    private Integer answerNumber;

    /**
     * 待批阅数
     */
    private Integer tobeReviewed;

    /**
     * 最后提交时间
     */
    private LocalDateTime commitedTime;

    /**
     * 最后批阅时间
     */
    private LocalDateTime reviewedTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;


}
