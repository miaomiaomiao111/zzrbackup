package com.xuecheng.agent.teaching;

import com.xuecheng.api.comment.model.CommentConditionModel;
import com.xuecheng.api.comment.model.CommentDTO;
import com.xuecheng.api.comment.model.CommentReplyDTO;
import com.xuecheng.common.constant.XcFeignServiceNameList;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * @author zzr
 * 教学服务 远程调用评论接口
 */

@FeignClient(value = XcFeignServiceNameList.XC_COMMENT_SERVICE)
public interface CommentApiAgent {

    @PostMapping("comment/l/list")
    public RestResponse<PageVO<CommentDTO>> list(@RequestParam PageRequestParams params, @RequestBody CommentConditionModel commentModel) ;

    @GetMapping(value = "comment/l/all-list",name = "分页查询评论 -运营平台")
    public RestResponse<PageVO<CommentDTO>> queryAllList(PageRequestParams params) ;

    @DeleteMapping(value = "comment/l/{commentId}",name = "删除评论")
    public RestResponse<Boolean> delComment(@PathVariable Long commentId) ;

    @DeleteMapping(value = "comment/l/batch-del",name = "批量删除评论")
    public RestResponse<Boolean>  batchDelComment(@RequestParam Long[] commentIds) ;

    @PostMapping("comment/l/commentReply")
    RestResponse<CommentReplyDTO> reply( @RequestBody CommentReplyDTO replyDTO, @RequestParam Long companyId);
}
