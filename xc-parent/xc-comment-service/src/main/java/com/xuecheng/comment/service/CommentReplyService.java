package com.xuecheng.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.comment.model.CommentReplyDTO;
import com.xuecheng.comment.entity.CommentReply;
import com.xuecheng.common.domain.response.RestResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2019-10-31
 */
public interface CommentReplyService extends IService<CommentReply> {
    RestResponse<CommentReplyDTO> commentReply (CommentReplyDTO replyDTO,Long companyId);
}
