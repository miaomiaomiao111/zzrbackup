package com.xuecheng.teaching.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.agent.teaching.CommentApiAgent;
import com.xuecheng.agent.teaching.ContentApiAgent;
import com.xuecheng.api.comment.model.CommentConditionModel;
import com.xuecheng.api.comment.model.CommentDTO;
import com.xuecheng.api.comment.model.CommentReplyDTO;
import com.xuecheng.api.content.model.dto.CoursePub;
import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.teaching.common.constant.TeachingErrorCode;
import com.xuecheng.teaching.convert.CompanyConvert;
import com.xuecheng.teaching.entity.Company;
import com.xuecheng.teaching.mapper.CompanyMapper;
import com.xuecheng.teaching.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService{

    @Autowired
    private ContentApiAgent coursePubService;
    @Autowired
    private CommentApiAgent commentApiAgent;
//    @Autowired
//    private CommentReplyApiAgent commentReplyApiAgent;

    public CompanyDTO getByTenantId(Long tenantId) {
        if (tenantId == null || tenantId <= 0) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Company::getTenantId, tenantId);
        Company company = this.getOne(queryWrapper);
        return CompanyConvert.INSTANCE.entity2dto(company);
    }


    /**
     * 分页查询课程评论列表
     *      业务分析:
     *          1开启事务 否
     *          2判断关键数据
     *          3判断业务数据
     *              用户:uaa 校验
     *              课程:
     *                  是否同一家机构,
     *              评论:
     *                  是否隐藏,是否是自家的机构
     *               评论回复:  是否隐藏
     *           4 远程调用 评论服务list接口 查询课程评论列表
     *             5转成dto 返回
     *
     * @param params 分页参数
     * @param model 课程评论查询参数
     * @param companyId 公司id
     * @return
     */
    @Override
    public PageVO<CommentDTO> queryCommentList(PageRequestParams params, CommentConditionModel model, Long companyId) {
        // 1开启事务 否
        // 2判断关键数据
        if (params.getPageNo() < 1) {
            params.setPageNo(PageRequestParams.DEFAULT_PAGE_NUM);
        }
        if (params.getPageSize() < 1) {
            params.setPageSize(PageRequestParams.DEFAULT_PAGE_SIZE);
        }
        //model 在comment模块有判断

        //判断机构id
        if (ObjectUtils.isEmpty(companyId)){
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }

        // 3判断业务数据
        //     课程: 是否同一家机构(在评论中判断可以)

        //评论是否隐藏,是否是自家的机构 (在comment list中写了)
        //评论回复:  是否隐藏 (在comment list中写了)


        //  4 远程调用 评论服务list接口 查询课程评论列表
        RestResponse<PageVO<CommentDTO>> response = commentApiAgent.list(params, model);
        if (!response.isSuccessful()){
           ExceptionCast.cast(TeachingErrorCode.E_130103);
        }

        //    5转成dto 返回
        PageVO<CommentDTO> pageVO = response.getResult();
        return pageVO;
    }

    /**
     * 课程评论回复
     *  分析业务:
     *      1开启事务
     *      2判断关键数据
     *      3判断业务数据
     *         评论 是否存在 是否禁止回复 是否显示
     *      4远程调用comment模块接口 回复评论
     *      5返回dto
     *
     * @param replyDTO 回复信息
     * @return
     */
    @Override
    @Transactional
    public CommentReplyDTO commentReply(CommentReplyDTO replyDTO ,Long companyId) {
        // 1开启事务
        // 2判断关键数据
        Long commentId = replyDTO.getCommentId();
        String replyText = replyDTO.getReplyText();
        if (ObjectUtils.isEmpty(companyId)||
        ObjectUtils.isEmpty(commentId)||
        StringUtil.isBlank(replyText)){
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }

        // 3判断业务数据
        //     评论 是否存在 是否禁止回复 是否显示  是否是当前机构 (在comment模块中实现)


        // 4远程调用comment模块接口 回复评论
        RestResponse<CommentReplyDTO> response = commentApiAgent.reply(replyDTO,companyId);
        CommentReplyDTO dto = response.getResult();
        // 5返回dto
        return dto;
    }

    /**
     * 分页查询课程评论列表 --运营平台
     * 业务分析:
     *  1开启事务 否
     *  2判断关键数据
     *  3判断业务数据
     *      评论: 是否隐藏
     *      论回复:  是否隐藏
     *   4 远程调用 评论服务list接口 查询课程评论列表
     *     5转成dto 返回
     * @param params
     * @return
     */
    @Override
    public PageVO<CommentDTO> queryCommentAllList(PageRequestParams params) {
        //2判断关键数据
        if (params.getPageNo() < 1) {
            params.setPageNo(PageRequestParams.DEFAULT_PAGE_NUM);
        }
        if (params.getPageSize() < 1) {
            params.setPageSize(PageRequestParams.DEFAULT_PAGE_SIZE);
        }
        //3判断业务数据
        //    评论:
        //        是否隐藏,是否是自家的机构
        //    评论回复:  是否隐藏
        //4 远程调用 评论服务list接口 查询课程评论列表
        RestResponse<PageVO<CommentDTO>> response = commentApiAgent.queryAllList(params);

        if (!response.isSuccessful()){
            ExceptionCast.cast(TeachingErrorCode.E_130103);
        }

        //    5转成dto 返回
        PageVO<CommentDTO> pageVO = response.getResult();
        return pageVO;
    }

    /**
     * 删除评论
     *      业务分析:
     *       1开启事务 是
     *       2判断关键数据
     *       3判断业务数据
     *           评论: 是否存在 , 评论下面有回复不能删除
     *           评论回复:  是否存在(隐藏不隐藏的都删)
     *       4 远程调用 评论模块删除评论接口
     * @param commentId 评论id
     */
    @Transactional
    public void deleteCommentById(Long commentId) {
        //1开启事务 是
        //2判断关键数据
        ExceptionCast.cast(ObjectUtils.isEmpty(commentId),CommonErrorCode.E_100101);

        //3判断业务数据
        //    评论: 是否存在 , 评论下面有回复不能删除
        //    评论回复:  是否存在(隐藏不隐藏的都删)


        //4 远程调用 评论模块删除评论接口
        RestResponse<Boolean> response = commentApiAgent.delComment(commentId);
        if (!response.isSuccessful()){
            ExceptionCast.cast(CommonErrorCode.E_999982);
        }

    }

    /**
     * 批量删除评论
     *       业务分析:
     *   1开启事务 是
     *   2判断关键数据
     *   3判断业务数据
     *       评论: 是否存在 ,  同时删除回复
     *       论回复: 是否存在,
     *   4 远程调用 评论模块删除评论接口
     * @param commentIds
     */
    @Transactional
    public void batchDelComment(Long[] commentIds) {
        //2判断关键数据
        ExceptionCast.cast(ObjectUtils.isEmpty(commentIds),CommonErrorCode.E_100101);

        //3判断业务数据
        //    评论: 是否存在 ,  同时删除回复
        //    论回复: 是否存在,


        //4 远程调用 评论模块批量删除评论接口
        RestResponse<Boolean> response = commentApiAgent.batchDelComment(commentIds);
        if (!response.isSuccessful()){
            ExceptionCast.cast(CommonErrorCode.E_999982);
        }

    }


}
