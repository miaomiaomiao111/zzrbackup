package com.xuecheng.comment.controller;


import com.xuecheng.api.comment.CommentAPI;
import com.xuecheng.api.comment.model.CommentConditionModel;
import com.xuecheng.api.comment.model.CommentDTO;
import com.xuecheng.comment.service.CommentService;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 课程评论功能
 */
@Slf4j
@RestController
@RequestMapping
public class CommentController implements CommentAPI {

    @Autowired
    private CommentService commentService;

    @PostMapping(value = "l/list",name = "分页查询评论")
    public RestResponse<PageVO<CommentDTO>> list( PageRequestParams params,@RequestBody CommentConditionModel commentModel) {
        PageVO<CommentDTO> pageVO = commentService.list(params, commentModel);

        return RestResponse.success(pageVO);
    }

    @Override
    @GetMapping(value = "l/all-list",name = "分页查询评论 -运营平台")
    public RestResponse<PageVO<CommentDTO>> queryAllList(PageRequestParams params) {

        RestResponse<PageVO<CommentDTO>> response =  commentService.queryAllList(params);
        return response;
    }


//    @PostMapping(value = "l/commentReply",name = "添加评论")
//    public CommentDTO comment( @RequestBody CommentDTO commentDTO) {
//        return commentService.addComment(commentDTO);
//    }


    @PostMapping(path = "praise/{commentId}",name = "评论点赞")
    public Boolean praise(@PathVariable Long commentId) {
        return true;
    }

    @DeleteMapping(value = "l/{commentId}",name = "删除评论")
    public RestResponse<Boolean> delComment(@PathVariable Long commentId) {
        return  commentService.delComment(commentId);
    }

    @DeleteMapping(value = "l/batch-del",name = "批量删除评论")
    public RestResponse<Boolean>  batchDelComment(@RequestParam Long[] commentIds) {
        List<Long> longs = Arrays.asList(commentIds);
        return commentService.delComments(longs);
    }

}

