package com.xuecheng.comment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 * @since 2019-10-31
 */
@Data
@TableName("comment_reply")
public class CommentReply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 回复记录
     */
    private Long commentId;

    private Long parentId;

    private Long userId;

    /**
     * 评论用户
     */
    private String userName;

    private String nickName;

    /**
     * 回复内容
     */
    private String replyText;

    /**
     * 回复时间
     */
    private LocalDateTime replyDate;

    /**
     * 0隐藏  1显示
     */
    private Integer status;

    /**
     * 是否禁止回复
     */
    private Integer forbidReply;

    /**
     * 点赞数量
     */
    private Integer praiseNum;


}
