package com.xuecheng.api.teaching;

import com.xuecheng.api.comment.model.CommentConditionModel;
import com.xuecheng.api.comment.model.CommentDTO;
import com.xuecheng.api.comment.model.CommentReplyDTO;
import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p></p>
 *
 * @Description:
 */
public interface CompanyApi {

    @ApiOperation("根据租户的id来获得公司的数据")
    CompanyDTO getByTenantId(Long tenantId);

    @ApiOperation("分页查询课程评论列表 -教学机构")
    PageVO<CommentDTO> queryCommentList(PageRequestParams params, CommentConditionModel model);

    @ApiOperation("课程评论回复 -教学机构")
    CommentReplyDTO commentReply(CommentReplyDTO replyDTO);

    @ApiOperation("分页查询课程评论列表 -运营平台")
    PageVO<CommentDTO> queryCommentAllList(PageRequestParams params);

    @ApiOperation("删除评论")
    void deleteCommentById(Long commentId);


    @ApiOperation("批量删除评论")
    void batchDelComment(@RequestParam(value = "commentIds[]") Long[] commentIds );


}
