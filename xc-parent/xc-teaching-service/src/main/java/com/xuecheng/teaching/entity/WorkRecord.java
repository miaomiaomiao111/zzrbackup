package com.xuecheng.teaching.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 作业提交记录
 * </p>
 *
 * @author itcast
 * @since 2019-09-19
 */
@Data
@TableName("work_record")
public class WorkRecord implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private Long userId;
    /**
     * 作业提交人
     */
    private String username;

    /**
     * 作业标识
     */
    private Long workId;

    /**
     * 课程发布标识
     */
    private Long coursePubId;


    /**
     * 课程计划Id
     */
    private Long teachplanId;

    /**
     * 课程计划名称
     */
    private String teachplanName;


    /**
     * 作业内容
     */
    private String question;

    /**
     * 完成内容
     */
    private String answer;

    /**
     * 类型(文字，文件等)
     */
    private String type;

    /**
     * 状态 WorkRecordCorrectionStatusEnum
     */
    private String status;

    /**
     * 批改人
     */
    private Long teacherId;

    /**
     * 批改意见
     */
    private String correctComment;

    /**
     * 批改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime correctionDate;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime changeDate;


}
