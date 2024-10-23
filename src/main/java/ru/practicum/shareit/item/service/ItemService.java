package ru.practicum.shareit.item.service;

import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItemsByUser(@NotNull Long userId);

    List<ItemDto> getItemByText(String text);

    ItemDto getItemById(Long itemId, @NotNull Long userId);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId);
}