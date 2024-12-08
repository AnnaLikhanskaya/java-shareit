package ru.practicum.item.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.service.CommentService;
import ru.practicum.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @Autowired
    public ItemController(@Qualifier("itemServiceDb") ItemService itemService,
                          CommentService commentService) {
        this.itemService = itemService;
        this.commentService = commentService;
    }


    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                        @RequestParam(required = false) Integer from,
                                        @RequestParam(required = false) Integer size) {
        return itemService.getItemsByUser(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByText(@RequestParam String text,
                                       @RequestParam(required = false) Integer from,
                                       @RequestParam(required = false) Integer size) {
        return itemService.getItemByText(text, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                               @PathVariable Long itemId) {
        return itemService.getItemById(itemId, userId);
    }

    @PostMapping
    public ItemDto creatItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                             @RequestBody ItemDto itemDto
    ) {
        return itemService.creatItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable @NotNull Long itemId) {
        return itemService.updateItem(userId, itemDto, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") @NotNull Long authorId,
            @Validated @RequestBody CommentDto commentDto
    ) {
        return commentService.addComment(itemId, authorId, commentDto);
    }
}