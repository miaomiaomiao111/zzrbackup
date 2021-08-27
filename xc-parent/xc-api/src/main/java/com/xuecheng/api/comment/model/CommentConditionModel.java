package com.xuecheng.api.comment.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.mapstruct.TargetType;

/**
 * 评论查询条件model
 *
 * @author zzr
 */
@Data
@ApiModel(value="CommentConditionModel", description="")
public class CommentConditionModel {
    @ApiModelProperty(value = "评价级别[1好评 0中评 -1差评]")
    private Integer level;
    @ApiModelProperty(value = "回复状态 Y/N")
    private String replyStatus;
    @ApiModelProperty(value = "评论对象,课程发布id")
    private Integer targetId;
    @ApiModelProperty(value = "评论对象[课程名称]")
    private String argetName;


    @ApiModelProperty(value = "评论来源[那个项目]")
    private String comeFrom;
    @ApiModelProperty(value = "对象类型")
    private String targetType;
    @ApiModelProperty(value = "评论归属于")
    private Long belongTo;
    @ApiModelProperty(value = "评论名称")
    private String targetName;
}
