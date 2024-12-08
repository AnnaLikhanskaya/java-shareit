package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.DateBookingDto;
import ru.practicum.shareit.booking.dto.model.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.NotExsistObject;
import ru.practicum.shareit.exception.OtherDataException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceDb implements ItemService, CommentService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        log.info("Получен запрос на список вещей по хозяину");
        return itemRepository.findByOwner(getUser(userId)).stream()
                .map(item -> fillItemDto(item, userId))
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemByText(String text) {
        log.info("Получен запрос на поиск вещи по названию или описанию");
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text.toLowerCase()).stream()
                .filter(Item::getAvailable)
                .map(item -> ItemMapper.toItemDto(item, getComments(item.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        log.info("Получен запрос на получение вещи");
        Item item = getItem(itemId);
        return ItemMapper.toItemDto(item,
                getComments(itemId),
                getLastBooking(item, userId),
                getNextBooking(item, userId));
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        log.info("Получен запрос на добавление вещи");
        itemDto.setOwner(getUser(userId));
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        log.info("Получен запрос на изменение вещи");
        User user = getUser(userId);
        Item item = getItem(itemId);

        if (!user.equals(itemRepository.getItemOwner(itemId))) {
            throw new OtherDataException("Редактировать может только владелец");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public CommentDto addComment(Long itemId, Long authorId, CommentDto commentDto) {
        log.info("Получен запрос на добавление комментария");
        Item item = getItem(itemId);
        User author = getUser(authorId);
        isBooker(item, author);
        isTheBookingCompleted(item, author);
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.toComment(commentDto, author, item);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private void isBooker(Item item, User user) {
        boolean isBooker = bookingRepository.findByBooker(user).stream()
                .anyMatch(booking -> booking.getItem().equals(item));
        if (!isBooker) {
            throw new InvalidRequestException("Невозможно оставить комментарий." +
                    "Пользователь с ID " + user.getId() + " никогда не бронировал вещь с ID " + item.getId());
        }
    }

    private void isTheBookingCompleted(Item item, User user) {
        List<Booking> bookings = bookingRepository.findByBookerAndItem(user, item);
        boolean isEnd = bookings.stream()
                .anyMatch(booking -> booking.getEnd().isBefore(LocalDateTime.now()));
        if (!isEnd) {
            throw new InvalidRequestException("Невозможно оставить комментарий. " +
                    "Бронирование с ID " + bookings.getFirst().getId() + " не завершено");
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotExsistObject("Невозможно найти пользователя с ID: " + userId));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotExsistObject("Невозможно найти вещь с ID: " + itemId));
    }

    private ItemDto fillItemDto(Item item, Long userId) {
        return ItemMapper.toItemDto(item,
                getComments(item.getId()),
                getLastBooking(item, userId),
                getNextBooking(item, userId));
    }

    private List<CommentDto> getComments(Long itemId) {
        return commentRepository.findByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private DateBookingDto getLastBooking(Item item, Long userId) {
        if (!item.getOwner().getId().equals(userId)) {
            return null;
        }
        List<Booking> bookings = bookingRepository.findByItemAndEndIsBefore(item, LocalDateTime.now());
        if (bookings.isEmpty()) {
            return null;
        }
        return BookingMapper.toDateBookingDto(bookings.get(bookings.size() - 1));
    }

    private DateBookingDto getNextBooking(Item item, Long userId) {
        if (!item.getOwner().getId().equals(userId)) return null;
        List<Booking> bookings = bookingRepository.findByItemAndStartIsAfter(item, LocalDateTime.now());
        if (bookings.isEmpty()) {
            return null;
        }
        return BookingMapper.toDateBookingDto(bookings.get(0));
    }
}