package ru.practicum.item.comment.mapper;

import ru.practicum.item.comment.model.Comment;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(CommentDto commentDto, User author, Item item) {
        return Comment.builder()
                .author(author)
                .created(commentDto.getCreated())
                .id(commentDto.getId())
                .text(commentDto.getText())
                .item(item)
                .build();
    }
}