package com.xuecheng.teaching.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.comment.model.CommentConditionModel;
import com.xuecheng.api.comment.model.CommentDTO;
import com.xuecheng.api.comment.model.CommentReplyDTO;
import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.teaching.entity.Company;

/**
 * 教育机构 服务类
 */
public interface CompanyService extends IService<Company> {

    /**
     * 根据租户ID获取机构信息
     * @param tenantId
     * @return
     */
    CompanyDTO getByTenantId(Long tenantId);

    /**
     * 分页查询课程评论列表
     * @param params 分页参数
     * @param model 课程评论查询参数
     * @param companyId 公司id
     * @return 课程评论分页数据
     */
    PageVO<CommentDTO> queryCommentList(PageRequestParams params, CommentConditionModel model, Long companyId);

    /**
     * 课程评论回复
     * @param replyDTO 回复信息
     * @param companyId 机构id
     * @return  CommentReplyDTO 回复信息
     */
    CommentReplyDTO commentReply(CommentReplyDTO replyDTO, Long companyId);

    /**
     * 分页查询 课程评论列表
     * @param params
     * @return
     */
    PageVO<CommentDTO> queryCommentAllList(PageRequestParams params);

    /**
     * 删除评论
     * @param commentId 评论id
     */
    void deleteCommentById(Long commentId);

    /**
     * 批量删除评论
     * @param commentIds
     */
    void batchDelComment(Long[] commentIds);
}
