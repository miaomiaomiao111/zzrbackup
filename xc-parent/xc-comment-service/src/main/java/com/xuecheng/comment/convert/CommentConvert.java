package com.xuecheng.comment.convert;

import com.xuecheng.api.comment.model.CommentDTO;
import com.xuecheng.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommentConvert {
    CommentConvert INSTANCE = Mappers.getMapper(CommentConvert.class);

    @Mappings({
            @Mapping(source = "id", target = "commentId"),
    })
    CommentDTO entity2dto(Comment entity);

    @Mapping(source = "commentId", target = "id")
    Comment dto2entity(CommentDTO dto);


    List<CommentDTO> entitys2dtos(List<Comment> list);

}
