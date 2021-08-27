package com.xuecheng.api.comment;


import com.xuecheng.api.comment.model.CommentReplyDTO;
import com.xuecheng.common.domain.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 评论回复api
 * @author zzr
 */
@Api("评论回复接口api")
public interface CommentReplyAPI {

    @ApiOperation("课程评论回复")
    RestResponse<CommentReplyDTO> reply(@Valid @RequestBody CommentReplyDTO replyDTO,Long companyId);
}
