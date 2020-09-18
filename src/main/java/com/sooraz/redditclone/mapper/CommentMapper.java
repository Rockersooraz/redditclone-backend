package com.sooraz.redditclone.mapper;

import com.sooraz.redditclone.dto.CommentDto;
import com.sooraz.redditclone.model.Comment;
import com.sooraz.redditclone.model.Post;
import com.sooraz.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment mapToComment(CommentDto commentDto, Post post, User user);


    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentDto mapToCommentDto(Comment comment);


}
