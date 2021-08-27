package com.xuecheng.comment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xuecheng.common.enums.common.CommonEnum;
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
@TableName("comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论对象
     */
    private Long targetId;

    private String targetName;

    /**
     * 对象类型
     */
    private String targetType;




    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评论用户
     */
    private String userName;
    /**
     * 评论用户昵称
     */
    private String nickName;

    private String userHead;

    private Integer starRank;

    /**
     * 评论内容
     */
    private String commentText;

    /**
     * 评论时间
     */
    private LocalDateTime commentDate;

    /**
     * 0隐藏  1显示
     */
    private Integer status;

    private Integer replyStatus = CommonEnum.DELETE_FLAG.getCodeInt();
    /**
     * 是否禁止回复(默认不禁止)
     */
    private Integer forbidReply = CommonEnum.DELETE_FLAG.getCodeInt();

    /**
     * 点赞数量
     */
    private Integer praiseNum;

    /**
     * 评论归属于
     */
    private Long belongTo;

    /**
     * 评论来源[即来自于那个应用]
     */
    private String comeFrom;

}
