package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.dto.comparator.BookingComparator;
import ru.practicum.shareit.booking.dto.model.Booking;
import ru.practicum.shareit.booking.dto.model.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceDb implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingServiceDb(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingOutputDto createBooking(BookingInputDto bookingInputDto, Long userId) {
        isTheTimeCorrect(bookingInputDto);

        Item item = getItem(bookingInputDto.getItemId());

        if (!item.getAvailable()) {
            throw new ItemUnavalibleException("Вещь снята с аренды");
        }
        if (userId.equals(item.getOwner().getId())) {
            throw new OtherDataException("Пользователь не может забронировать свою вещь");
        }

        User user = getUser(userId);
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(bookingInputDto.getStart());
        booking.setEnd(bookingInputDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);

        log.info("Бронирование создано с ID: {}", savedBooking.getId());

        return BookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public BookingOutputDto updateApprove(Long bookingId, Boolean approved, Long userId) {

        Booking booking = getBooking(bookingId);
        User user = getUser(userId);
        User owner = booking.getItem().getOwner();
        if (!user.equals(owner)) {
            throw new OtherDataException("Статус бронирования может изменить только владелец");
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new InvalidRequestException("Статус бронирования можно изменить только во время его ожидания");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking savedBooking = bookingRepository.save(booking);

        log.info("Бронирование изменено с ID: {}", savedBooking.getId());
        return BookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public BookingOutputDto getBookingInfo(Long bookingId, Long userId) {
        log.info("Получен запрос на получение информации о бронировании  с ID: {}", bookingId);

        User user = getUser(userId);
        Booking booking = getBooking(bookingId);
        User owner = booking.getItem().getOwner();
        User booker = booking.getBooker();
        boolean canGetInfo = owner.equals(user) || booker.equals(user);
        if (!canGetInfo) {
            throw new OtherDataException("Просматривать информацию о бронировании могут владелец или бронирующий");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingOutputDto> getAllBookings(Long bookerId, BookingState state) {
        User booker = getUser(bookerId);
        log.info("Получен запрос на получение списка бронирований");
        switch (state) {
            case ALL:
                return convertBookings(bookingRepository.findByBooker(booker));
            case CURRENT:
                return convertBookings(bookingRepository.findCurrentByBooker(booker));
            case PAST:
                return convertBookings(bookingRepository.findByBookerAndEndIsBefore(booker, LocalDateTime.now()));
            case FUTURE:
                return convertBookings(bookingRepository.findByBookerAndStartIsAfter(booker, LocalDateTime.now()));
            case WAITING:
                return convertBookings(bookingRepository.findByBookerAndStatus(booker, BookingStatus.WAITING));
            case REJECTED:
                return convertBookings(bookingRepository.findByBookerAndStatus(booker, BookingStatus.REJECTED));
        }

        throw new InvalidRequestException("Не существующий статус" + state);
    }

    @Override
    public List<BookingOutputDto> getAllBookingsForOwner(Long ownerId, BookingState state) {
        User owner = getUser(ownerId);
        List<Item> items = itemRepository.findByOwner(owner);
        log.info("Получен запрос на получение списка бронирований по владельцу");
        return items.stream()
                .flatMap((item) -> getAllBookingsForItem(item, state).stream())
                .collect(Collectors.toList());
    }

    private List<BookingOutputDto> getAllBookingsForItem(Item item, BookingState state) {
        switch (state) {
            case ALL:
                return convertBookings(bookingRepository.findByItem(item));
            case CURRENT:
                return convertBookings(bookingRepository.findCurrentByItem(item));
            case PAST:
                return convertBookings(bookingRepository.findByItemAndEndIsBefore(item, LocalDateTime.now()));
            case FUTURE:
                return convertBookings(bookingRepository.findByItemAndStartIsAfter(item, LocalDateTime.now()));
            case WAITING:
                return convertBookings(bookingRepository.findByItemAndStatus(item, BookingStatus.WAITING));
            case REJECTED:
                return convertBookings(bookingRepository.findByItemAndStatus(item, BookingStatus.REJECTED));
            default:
                throw new UnknownStateException("Unknown state: " + state);
        }
    }

    private List<BookingOutputDto> convertBookings(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .sorted(new BookingComparator().reversed())
                .collect(Collectors.toList());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Невозможно найти пользователя с ID: " + userId));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotExsistObject("Невозможно найти вещь с ID: " + itemId));
    }

    private Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotExsistObject("Невозможно найти бронирование с ID: " + bookingId));
    }

    private void isTheTimeCorrect(BookingInputDto bookingInputDto) {
        if (bookingInputDto.getEnd().isBefore(bookingInputDto.getStart())) {
            throw new InvalidRequestException("Конец бронирования не может быть раньше начала");
        }
    }
}