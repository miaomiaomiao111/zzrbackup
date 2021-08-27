package com.xuecheng.comment.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.comment.model.CommentConditionModel;
import com.xuecheng.api.comment.model.CommentDTO;
import com.xuecheng.comment.common.constant.CommentErrorCode;
import com.xuecheng.comment.convert.CommentConvert;
import com.xuecheng.comment.convert.CommentReplyConvert;
import com.xuecheng.comment.entity.Comment;
import com.xuecheng.comment.entity.CommentReply;
import com.xuecheng.comment.mapper.CommentMapper;
import com.xuecheng.comment.mapper.CommentReplyMapper;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.enums.common.CommonEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2019-10-31
 */
@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentReplyMapper commentReplyMapper;


    public CommentDTO addComment(CommentDTO commentDTO) {
        Comment comment = CommentConvert.INSTANCE.dto2entity(commentDTO);
        comment.setCommentDate(LocalDateTime.now());
        comment.setStatus(CommonEnum.USING_FLAG.getCodeInt());
        this.save(comment);
        return CommentConvert.INSTANCE.entity2dto(comment);
    }


    @Transactional(readOnly = true)
    public PageVO<CommentDTO> list(PageRequestParams params, CommentConditionModel commentModel) {

//        ExceptionCast.cast(commentModel == null || StringUtils.isBlank(commentModel.getComeFrom()), CommonErrorCode.E_100101);

        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        //评论对象,课程发布id
        wrapper.eq(commentModel.getTargetId() != null, Comment::getTargetId, commentModel.getTargetId());
        //对象类型
        wrapper.eq(StringUtils.isNotBlank(commentModel.getTargetType()), Comment::getTargetType, commentModel.getTargetType());
        //机构
        wrapper.eq(commentModel.getBelongTo() != null, Comment::getBelongTo, commentModel.getBelongTo());

        //评论名称
        if (StringUtils.isNotBlank(commentModel.getTargetName())) {
            wrapper.like(Comment::getTargetName, commentModel.getTargetName());
        }
        //回复状态不为空且 为Y
        if (StringUtils.isNotBlank(commentModel.getReplyStatus()) && commentModel.getReplyStatus().equalsIgnoreCase("Y")) {
            wrapper.eq(Comment::getReplyStatus, CommonEnum.USING_FLAG.getCode());
        } else if (StringUtils.isNotBlank(commentModel.getReplyStatus()) && commentModel.getReplyStatus().equalsIgnoreCase("N")) {
            wrapper.eq(Comment::getReplyStatus, CommonEnum.DELETE_FLAG.getCode());
        }

        //评价级别[1好评 0中评 -1差评]
        if (commentModel.getLevel() != null) {
            if (commentModel.getLevel().intValue() == 1) {
                wrapper.gt(Comment::getStarRank, 3);
            } else if (commentModel.getLevel().intValue() == 0) {
                wrapper.eq(Comment::getStarRank, 3);
            } else if (commentModel.getLevel().intValue() == -1) {
                wrapper.lt(Comment::getStarRank, 3);
            }
        }
        //评论来源[即来自于那个应用]
//        wrapper.eq(Comment::getComeFrom, commentModel.getComeFrom());
        //评论状态 必须是显示
        wrapper.eq(Comment::getStatus, CommonEnum.USING_FLAG.getCode());
        IPage<Comment> iPage = this.page(new Page<>(params.getPageNo(), params.getPageSize()), wrapper);

        PageVO<CommentDTO> result = new PageVO<>(iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
        if (iPage.getRecords() != null && iPage.getRecords().size() > 0) {
            List<CommentDTO> commentDTOS = CommentConvert.INSTANCE.entitys2dtos(iPage.getRecords());
            for (CommentDTO commentDTO : commentDTOS) {
                LambdaQueryWrapper<CommentReply> wrapper_1 = new LambdaQueryWrapper<>();
                wrapper_1.eq(CommentReply::getCommentId, commentDTO.getCommentId());
                //根据显示态显示
                wrapper_1.eq(CommentReply::getStatus, CommonEnum.USING_FLAG.getCode());
                List<CommentReply> commentReplies = commentReplyMapper.selectList(wrapper_1);

                commentDTO.setReplyDTOList(CommentReplyConvert.INSTANCE.entitys2dtos(commentReplies).toArray());
            }
            result.setItems(commentDTOS);
        }
        return result;
    }


    /*
    简单点，不用树形的层级机构展示评论及回复
    private List<CommentReplyDTO> makeCommentReplyTree(List<CommentReply> commentReplies) {
        List<CommentReplyDTO> replyDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(commentReplies)) {
            for (CommentReply commentReply : commentReplies) {
                if (commentReply.getParentId().longValue() == 0) {
                    CommentReplyDTO replyDTO = CommentReplyConvert.INSTANCE.entity2dto(commentReply);
                    replyDTOS.add(replyDTO);
                    recurCommentReplyTree(replyDTO, commentReplies);
                }
            }
        }
        return replyDTOS;
    }

    private void recurCommentReplyTree(CommentReplyDTO dto, List<CommentReply> commentReplies) {
        for (CommentReply cr : commentReplies) {
            if(cr.getParentId().equals(dto.getReplyId())) {
                dto.getReplyDTOList().add(CommentReplyConvert.INSTANCE.entity2dto(cr));
            }
        }
        if (!CollectionUtils.isEmpty(dto.getReplyDTOList())) {
            for (CommentReplyDTO tmp : dto.getReplyDTOList()) {
                recurCommentReplyTree(tmp, commentReplies);
            }
        }
    }*/


    @Transactional
    public RestResponse<Boolean> delComment(Long commentId) {
        if (commentId == null){
            RestResponse.validfail(CommonErrorCode.E_100101);
        }

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getId, commentId);
        Comment comment = this.getOne(queryWrapper);
        //评论不存在
        if (comment == null) {
            RestResponse.validfail(CommentErrorCode.E_180002);
        }
        Boolean b1 = removeById(commentId);

        //删除评论回复
        LambdaQueryWrapper<CommentReply> queryWrapper_1 = new LambdaQueryWrapper<>();
        queryWrapper_1.eq(CommentReply::getCommentId, commentId);
        int b2 = commentReplyMapper.delete(queryWrapper_1);

        if (!b1){
            RestResponse.validfail(CommentErrorCode.E_180003);
        }
        return RestResponse.success(b1);
    }


    @Transactional
    public RestResponse<Boolean> delComments(List<Long> commentIds) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Comment::getId, commentIds);
        List<Comment> list = this.list(queryWrapper);
        //删除的评论不存在
        if (CollectionUtils.isEmpty(list)) {
            RestResponse.validfail(CommonErrorCode.E_100101);
        }
        boolean removed = this.remove(queryWrapper);

        //真滴优雅
        List<Long> collect = list.stream().map(Comment::getId).collect(Collectors.toList());

        LambdaQueryWrapper<CommentReply> queryWrapper_1 = new LambdaQueryWrapper<>();
        queryWrapper_1.in(CommentReply::getCommentId, collect);
        commentReplyMapper.delete(queryWrapper_1);

        if (!removed){
            RestResponse.validfail(CommentErrorCode.E_180003);
        }

        return RestResponse.success(removed);
    }

    /**
     * 分页查询评论列表 --运营平台
     *         直接分页查询
     * @param params
     * @return
     */
    @Override
    public RestResponse<PageVO<CommentDTO>> queryAllList(PageRequestParams params) {

        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        //评论状态 必须是显示
        wrapper.eq(Comment::getStatus, CommonEnum.USING_FLAG.getCode());

        IPage<Comment> iPage = this.page(new Page<>(params.getPageNo(), params.getPageSize()), wrapper);

        PageVO<CommentDTO> result = new PageVO<>(iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
        if (iPage.getRecords() != null && iPage.getRecords().size() > 0) {

            List<CommentDTO> commentDTOS = CommentConvert.INSTANCE.entitys2dtos(iPage.getRecords());

            //把评论回复装到评论中去
            for (CommentDTO commentDTO : commentDTOS) {
                LambdaQueryWrapper<CommentReply> wrapper_1 = new LambdaQueryWrapper<>();

                wrapper_1.eq(CommentReply::getCommentId, commentDTO.getCommentId());
                //根据显示态显示
                wrapper_1.eq(CommentReply::getStatus, CommonEnum.USING_FLAG.getCode());

                List<CommentReply> commentReplies = commentReplyMapper.selectList(wrapper_1);

                commentDTO.setReplyDTOList(CommentReplyConvert.INSTANCE.entitys2dtos(commentReplies).toArray());
            }
            result.setItems(commentDTOS);
        }
        return RestResponse.success(result);
    }

}
