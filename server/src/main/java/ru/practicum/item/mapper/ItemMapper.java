package ru.practicum.item.mapper;


import ru.practicum.booking.dto.DateBookingDto;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemForRequestDto;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;

import java.util.List;

import static ru.practicum.user.mapper.UserMapper.toUser;
import static ru.practicum.user.mapper.UserMapper.toUserDto;

public class ItemMapper {
    public static ItemDto toItemDto(
            Item item,
            List<CommentDto> comments,
            DateBookingDto lastBooking,
            DateBookingDto nextBooking
    ) {
        ItemRequest request = item.getRequest();
        Long requestId = request != null ? request.getId() : null;
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(toUserDto(item.getOwner()))
                .requestId(requestId)
                .comments(comments)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();
    }

    public static ItemDto toItemDto(Item item) {
        ItemRequest request = item.getRequest();
        Long requestId = request != null ? request.getId() : null;
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .owner(toUserDto(item.getOwner()))
                .build();
    }

    public static ItemDto toItemDto(Item item, List<CommentDto> comments) {
        ItemRequest request = item.getRequest();
        Long requestId = request != null ? request.getId() : null;
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(toUserDto(item.getOwner()))
                .requestId(requestId)
                .comments(comments)
                .build();
    }

    public static Item toItem(ItemDto itemDto, ItemRequest request) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(toUser(itemDto.getOwner()))
                .request(request)
                .build();
    }

    public static ItemForRequestDto toItemForRequestDto(Item item) {
        return ItemForRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest().getId())
                .build();
    }
}