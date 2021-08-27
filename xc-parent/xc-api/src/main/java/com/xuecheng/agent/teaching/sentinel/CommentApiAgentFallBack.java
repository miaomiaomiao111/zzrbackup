package com.xuecheng.agent.teaching.sentinel;

import com.xuecheng.agent.teaching.CommentApiAgent;
import com.xuecheng.api.comment.model.CommentConditionModel;
import com.xuecheng.api.comment.model.CommentDTO;
import com.xuecheng.api.comment.model.CommentReplyDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * 教学服务 远程调用评论接口 熔断降级类
 * @author zzr
 */
@Component
public class CommentApiAgentFallBack implements CommentApiAgent {
    @Override
    public RestResponse<PageVO<CommentDTO>> list(PageRequestParams params, CommentConditionModel commentModel) {
        return RestResponse.validfail(CommonErrorCode.E_999982);
    }

    @Override
    public RestResponse<PageVO<CommentDTO>> queryAllList(PageRequestParams params) {

        return RestResponse.validfail(CommonErrorCode.E_999982);
    }

    @Override
    public RestResponse<Boolean> delComment(Long commentId) {
        return RestResponse.validfail(CommonErrorCode.E_999982);
    }

    @Override
    public RestResponse<Boolean> batchDelComment(Long[] commentIds) {
        return RestResponse.validfail(CommonErrorCode.E_999982);
    }
    @Override
    public RestResponse<CommentReplyDTO> reply(@Valid CommentReplyDTO replyDTO, Long companyId) {
        return RestResponse.validfail(CommonErrorCode.E_999983);
    }
}
