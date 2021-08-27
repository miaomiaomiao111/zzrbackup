package com.xuecheng.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.comment.model.CommentDTO;
import com.xuecheng.api.comment.model.CommentConditionModel;
import com.xuecheng.comment.entity.Comment;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2019-10-31
 */
public interface CommentService extends IService<Comment> {

    /**
     * 分页查询评论内容， 可根据评论对象，可根据评论归属对象查询
     * @param params
     * @param commentModel
     * @return
     */
    PageVO<CommentDTO> list(PageRequestParams params, CommentConditionModel commentModel);


    CommentDTO addComment(CommentDTO commentDTO);


    RestResponse<Boolean> delComment(Long commentId);

    RestResponse<Boolean> delComments(List<Long> commentIds);

    /**
     * 分页查询评论内容， --运营平台
     * @param params
     * @return
     */
    RestResponse<PageVO<CommentDTO>> queryAllList(PageRequestParams params);
}
