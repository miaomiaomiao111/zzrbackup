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
@ApiModel(value="CommentReplyDTO", description="")
public class CommentReplyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long replyId;

    @ApiModelProperty(value = "回复记录")
    private Long commentId;

    private Long parentId;

    private Long userId;

    @ApiModelProperty(value = "评论用户")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "回复内容")
    private String replyText;

    @ApiModelProperty(value = "回复时间")
    private LocalDateTime replyDate;

    @ApiModelProperty(value = "0隐藏  1显示")
    private Integer status;

    @ApiModelProperty(value = "是否禁止回复")
    private Integer forbidReply;

    @ApiModelProperty(value = "点赞数量")
    private Integer praiseNum;


}
