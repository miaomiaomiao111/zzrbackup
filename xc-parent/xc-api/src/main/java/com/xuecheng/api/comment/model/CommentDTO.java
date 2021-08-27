package com.xuecheng.api.comment.model;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
@ApiModel(value="CommentDTO", description="")
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long commentId;

    @ApiModelProperty(value = "评论对象")
    private Long targetId;

    @ApiModelProperty(value = "评论名称")
    private String targetName;

    @ApiModelProperty(value = "对象类型")
    private String targetType;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "评论用户")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String userHead;

    @ApiModelProperty(value = "评论内容")
    private String commentText;

    @ApiModelProperty(value = "评论时间")
    private LocalDateTime commentDate;

    @ApiModelProperty(value = "评几星")
    private Integer starRank;

    @ApiModelProperty(value = "0隐藏  1显示")
    private Integer status;

    @ApiModelProperty(value = "是否评论")
    private Integer replyStatus;

    @ApiModelProperty(value = "是否禁止回复")
    private Integer forbidReply;

    @ApiModelProperty(value = "点赞数量")
    private Integer praiseNum;

    @ApiModelProperty(value = "评论归属于")
    private Long belongTo;

    @ApiModelProperty(value = "评论来源[那个项目]")
    private String comeFrom;

    @ApiModelProperty(value = "回复List")
    private Object[] replyDTOList;

}
