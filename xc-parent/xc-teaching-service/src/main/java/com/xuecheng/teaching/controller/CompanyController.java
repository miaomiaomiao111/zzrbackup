package com.xuecheng.teaching.controller;

import com.xuecheng.api.comment.model.CommentConditionModel;
import com.xuecheng.api.comment.model.CommentDTO;
import com.xuecheng.api.comment.model.CommentReplyDTO;
import com.xuecheng.api.teaching.CompanyApi;
import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.teaching.common.utils.UAASecurityUtil;
import com.xuecheng.teaching.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 教育机构 前端控制器
 */
@Slf4j
@RestController
public class CompanyController implements CompanyApi {

    @Autowired
    private CompanyService companyService;

    @GetMapping("l/company/{tenantId}")
    public CompanyDTO getByTenantId(@PathVariable("tenantId") Long tenantId) {
        return companyService.getByTenantId(tenantId);
    }

    @PostMapping(value = "course-comment/list",name = "查询课程评论列表 -教学机构")
    public PageVO<CommentDTO> queryCommentList( PageRequestParams params, @RequestBody CommentConditionModel model) {
        Long companyId = UAASecurityUtil.getCompanyId();

        PageVO<CommentDTO> pageVO= companyService.queryCommentList(params,model,companyId);
        return pageVO;
    }
//todo 课程回复
    @PostMapping(value = "course-comment-reply",name = "课程评论回复")
    public CommentReplyDTO commentReply(@RequestBody CommentReplyDTO replyDTO) {
        Long companyId = UAASecurityUtil.getCompanyId();

        CommentReplyDTO dto=  companyService.commentReply(replyDTO,companyId);
        return dto;
    }

    @PostMapping(value = "m/course-comment/list-all",name = "查询课程评论列表 -运营平台")
    public PageVO<CommentDTO> queryCommentAllList(PageRequestParams params) {
        PageVO<CommentDTO> pageVO=  companyService.queryCommentAllList(params);
        return pageVO;
    }

    @DeleteMapping(value = "m/course-comment/{commentId}",name = "删除评论 -运营平台")
    public void deleteCommentById(@PathVariable Long commentId) {
        companyService.deleteCommentById(commentId);
    }

    @DeleteMapping(value = "m/course-comment/batch-del",name = "批量删除评论 -运营平台")
    public void batchDelComment(@RequestParam(value = "commentIds") Long[] commentIds) {
        companyService.batchDelComment(commentIds);
    }


}
