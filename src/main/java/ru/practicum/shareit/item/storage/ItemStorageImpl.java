package ru.practicum.shareit.item.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotExsistObject;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Repository
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private long id = 1;

    @Override
    public List<Item> getItemsByUser(Long userId) {
        log.info("Получен запрос на вывод вещей определенного пользователя");
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemByText(String text) {
        log.info("Получен запрос на поиск вещи по названию или описанию");
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> mergeNameAndDesc(item.getId()).toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Long itemId) {
        Item item = checkAvailability("найти", itemId);
        log.info("Получен запрос на получение вещи по id");
        return item;
    }

    @Override
    public Item addItem(Item item) {
        item.setId(getId());
        items.put(item.getId(), item);
        log.info("Получен запрос на добавление вещи");
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        Item exsistingItem = checkAvailability("изменить", item.getId());
        checkOwner(exsistingItem, item.getOwner().getId());
        if (item.getName() != null && !item.getName().isBlank()) {
            exsistingItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            exsistingItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            exsistingItem.setAvailable(item.getAvailable());
        }
        log.info("Получен запрос на изменение характеристик вещи");
        return exsistingItem;
    }

    private long getId() {
        return id++;
    }

    private Item checkAvailability(String operation, long id) {
        String message = String.format("Невозможно %s. Вещь не найдена!", operation);
        Item item = items.get(id);
        if (!items.containsKey(id)) {
            throw new NotExsistObject(message);
        }
        return item;
    }

    private void checkOwner(Item item, long userId) {
        if (!Objects.equals(item.getOwner(), userId)) {
            throw new NotExsistObject("Вещь принадлежит другому пользователю!");
        }
    }

    private String mergeNameAndDesc(Long itemId) {
        Item item = items.get(itemId);
        String merge = item.getName() + " " + item.getDescription();
        return merge.toLowerCase();
    }
}