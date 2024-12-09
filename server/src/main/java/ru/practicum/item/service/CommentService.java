package ru.practicum.item.service;


import ru.practicum.item.dto.CommentDto;

public interface CommentService {
    CommentDto addComment(Long itemId, Long authorId, CommentDto commentDto);
}