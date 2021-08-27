package com.xuecheng.api.comment;

import com.xuecheng.api.comment.model.CommentConditionModel;
import com.xuecheng.api.comment.model.CommentDTO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author zzr
 * 评论系统接口api
 */
@Api("评论接口api")
public interface CommentAPI {

    @ApiOperation("分页查询评论列表接口 --教学机构")
    public RestResponse<PageVO<CommentDTO>> list(PageRequestParams params,  CommentConditionModel commentModel);

    @ApiOperation("分页查询评论列表接口 --运营平台")
    public RestResponse<PageVO<CommentDTO>> queryAllList(PageRequestParams params);

    @ApiOperation("删除评论 --运营平台")
    public  RestResponse<Boolean> delComment( Long commentId) ;

    @ApiOperation("批量删除评论 --运营平台")
    public RestResponse<Boolean> batchDelComment(Long[] commentIds) ;

}
