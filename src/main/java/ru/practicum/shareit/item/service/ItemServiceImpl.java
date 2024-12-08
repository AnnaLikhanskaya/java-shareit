package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Autowired
    public ItemServiceImpl(@Qualifier("userStorageImpl") UserStorage userStorage,
                           @Qualifier("itemStorageImpl") ItemStorage itemStorage) {
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        userStorage.checkUserAvailability("найти", userId);
        return itemStorage.getItemsByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemByText(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.getItemByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        userStorage.checkUserAvailability("найти", userId);
        Item item = itemStorage.getItemById(itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userStorage.checkUserAvailability("найти", userId);
        itemDto.setOwner(userStorage.getUserById(userId));
        Item item = itemStorage.addItem(ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        userStorage.checkUserAvailability("найти", userId);
        itemDto.setOwner(userStorage.getUserById(userId));
        itemDto.setId(itemId);
        Item item = itemStorage.updateItem(ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }
}