package com.xuecheng.comment.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.comment.model.CommentReplyDTO;
import com.xuecheng.comment.common.constant.CommentErrorCode;
import com.xuecheng.comment.convert.CommentReplyConvert;
import com.xuecheng.comment.entity.Comment;
import com.xuecheng.comment.entity.CommentReply;
import com.xuecheng.comment.mapper.CommentMapper;
import com.xuecheng.comment.mapper.CommentReplyMapper;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.enums.common.CommonEnum;
import com.xuecheng.common.exception.ExceptionCast;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 *  评论回复 服务实现类
 *
 * @author itcast
 * @since 2019-10-31
 */
@Slf4j
@Service
public class CommentReplyServiceImpl extends ServiceImpl<CommentReplyMapper, CommentReply> implements CommentReplyService {

    @Autowired
    private CommentMapper commentMapper;


    @Transactional
    public RestResponse<CommentReplyDTO> commentReply (CommentReplyDTO replyDTO,Long companyId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        //评论是否存在  是否为当前机构
        queryWrapper.eq(Comment::getId, replyDTO.getCommentId());
        queryWrapper.eq(Comment::getBelongTo,companyId);

        Comment comment = commentMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(comment)){
            RestResponse.validfail(CommentErrorCode.E_180002);
        }
        //是否禁止回复
        if (comment.getForbidReply() == CommonEnum.USING_FLAG.getCodeInt()) {
            RestResponse.validfail(CommentErrorCode.E_180100);
        }
        //评论显示
        comment.setReplyStatus(CommonEnum.USING_FLAG.getCodeInt());
        commentMapper.updateById(comment);

        CommentReply commentReply = CommentReplyConvert.INSTANCE.dto2entity(replyDTO);
        commentReply.setStatus(CommonEnum.USING_FLAG.getCodeInt());
        commentReply.setReplyDate(LocalDateTime.now());
        boolean result = this.save(commentReply);
        if (!result){
            RestResponse.validfail(CommentErrorCode.E_180001);
        }
        CommentReplyDTO dto = CommentReplyConvert.INSTANCE.entity2dto(commentReply);
        return  RestResponse.success(dto);
    }

}
