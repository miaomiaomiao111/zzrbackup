package com.xuecheng.learning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 选课记录
 * </p>
 *
 * @author itcast
 */
@Data
@TableName("course_record")
public class CourseRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    /**
     * 选课人
     */
    private String userName;

    /**
     * 课程所属机构标识
     */
    private Long companyId;

    /**
     * 课程标识
     */
    private Long courseId;

    /**
     * 课程发布ID
     */
    private Long coursePubId;

    /**
     * 课程名称
     */
    private String coursePubName;

    /**
     * 教育模式(common普通，record 录播，live直播等）
     */
    private String teachmode;

    /**
     * 课程有效期（不论点播或直播，在该期限内有效）
     */
    private LocalDateTime startTime;

    /**
     * 课程有效期（不论点播或直播，在该期限内有效）
     */
    private LocalDateTime endTime;

    /**
     * 正在学习的课程计划章节Id
     */
    private Long teachplanId;

    /**
     * 正在学习的课程计划章节名称
     */
    private String teachplanName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;

    /**
     * 是否已支付
     */
    private Integer paid;


}
