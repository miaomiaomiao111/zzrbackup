package com.xuecheng.comment.controller;


import com.xuecheng.api.comment.CommentReplyAPI;
import com.xuecheng.api.comment.model.CommentReplyDTO;
import com.xuecheng.comment.service.CommentReplyService;
import com.xuecheng.common.domain.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author itcast
 * @since 2019-10-31
 */
@Slf4j
@RestController
@RequestMapping
public class CommentReplyController implements CommentReplyAPI {

    @Autowired
    private CommentReplyService commentReplyService;


    @PostMapping("l/commentReply")
    public RestResponse<CommentReplyDTO> reply( @RequestBody CommentReplyDTO replyDTO,@RequestParam Long companyId) {
        return commentReplyService.commentReply(replyDTO,companyId);
    }

}
