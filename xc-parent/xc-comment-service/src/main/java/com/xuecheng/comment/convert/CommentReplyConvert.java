package com.xuecheng.comment.convert;

import com.xuecheng.api.comment.model.CommentReplyDTO;
import com.xuecheng.comment.entity.CommentReply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommentReplyConvert {

    CommentReplyConvert INSTANCE = Mappers.getMapper(CommentReplyConvert.class);

    @Mappings({
            @Mapping(source = "id", target = "replyId"),
    })
    CommentReplyDTO entity2dto(CommentReply entity);

    @Mapping(source = "replyId", target = "id")
    CommentReply dto2entity(CommentReplyDTO dto);


    List<CommentReplyDTO> entitys2dtos(List<CommentReply> entitys);
}
