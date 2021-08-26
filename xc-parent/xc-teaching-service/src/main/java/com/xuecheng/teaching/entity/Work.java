package com.xuecheng.teaching.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 作业
 * </p>
 *
 * @author itcast
 * @since 2019-09-18
 */
@Data
@TableName("work")
public class Work implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 机构ID
     */
    private Long companyId;

    /**
     * 机构名称
     */
    private String companyName;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String question;
    /**
     //课程发布标识
    private Long coursePubId;

     //课程计划标识
    private Long teachplanId;

     // 课程标识
    private Long courseId;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课程大纲名称")
    private String teachplanName;*/



    /**
     * 创建人
     */
    private String username;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;


}
