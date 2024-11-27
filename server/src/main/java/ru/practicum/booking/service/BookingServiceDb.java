package ru.practicum.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.booking.comparator.BookingComparator;
import ru.practicum.booking.dto.BookingInputDto;
import ru.practicum.booking.dto.BookingOutputDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.booking.dto.BookingStatus;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.storage.BookingRepository;
import ru.practicum.exception.*;
import ru.practicum.item.model.Item;
import ru.practicum.item.paging.CustomPageRequest;
import ru.practicum.item.storage.ItemRepository;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.user.mapper.UserMapper.toUserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceDb implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingOutputDto createBooking(BookingInputDto bookingInputDto, Long userId) {
        isTheTimeCorrect(bookingInputDto);
        User user = getUser(userId);
        Item item = getItem(bookingInputDto.getItemId());

        if (!item.getAvailable()) {
            throw new InvalidRequestException("Вещь снята с аренды");
        }
        if (userId.equals(item.getOwner().getId())) {
            throw new OtherDataException("Пользователь не может забронировать свою вещь");
        }


        BookingOutputDto bookingOutputDto = BookingOutputDto.builder()
                .status(BookingStatus.WAITING)
                .end(bookingInputDto.getEnd())
                .start(bookingInputDto.getStart())
                .item(toItemDto(item))
                .booker(toUserDto(user))
                .build();
        log.info("Бронирование создано");
        ItemRequest request = item.getRequest();
        return BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toBooking(bookingOutputDto, request)));
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
        log.info("Бронирование изменено");
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingOutputDto getBookingInfo(Long bookingId, Long userId) {
        User user = getUser(userId);
        Booking booking = getBooking(bookingId);
        User owner = booking.getItem().getOwner();
        User booker = booking.getBooker();
        boolean canGetInfo = owner.equals(user) || booker.equals(user);
        if (!canGetInfo) {
            throw new OtherDataException("Просматривать информацию о бронировании могут владелец или бронирующий");
        }
        log.info("Получен запрос на получение информации о бронировании");
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingOutputDto> getAllBookings(Long bookerId, BookingState state, Integer from, Integer size) {
        User booker = getUser(bookerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        switch (state) {
            case ALL:
                return convertBookings(bookingRepository.findByBooker(booker, pageable));
            case CURRENT:
                return convertBookings(bookingRepository.findCurrentByBooker(booker, pageable));
            case PAST:
                return convertBookings(bookingRepository.findByBookerAndEndIsBefore(booker, LocalDateTime.now(), pageable));
            case FUTURE:
                return convertBookings(bookingRepository.findByBookerAndStartIsAfter(booker, LocalDateTime.now(), pageable));
            case WAITING:
                return convertBookings(bookingRepository.findByBookerAndStatus(booker, BookingStatus.WAITING, pageable));
            case REJECTED:
                return convertBookings(bookingRepository.findByBookerAndStatus(booker, BookingStatus.REJECTED, pageable));
        }

        throw new UnknownStateException("Unknown state: UNSUPPORTED_STATUS");
    }


    @Override
    public List<BookingOutputDto> getAllBookingsForOwner(Long ownerId, BookingState state, Integer from, Integer size) {
        User owner = getUser(ownerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        switch (state) {
            case ALL:
                return convertBookings(bookingRepository.findByOwner(owner, pageable));
            case CURRENT:
                return convertBookings(bookingRepository.findCurrentByOwner(owner, pageable));
            case PAST:
                return convertBookings(bookingRepository.findByOwnerAndEndIsBefore(owner, LocalDateTime.now(), pageable));
            case FUTURE:
                return convertBookings(bookingRepository.findByOwnerAndStartIsAfter(owner, LocalDateTime.now(), pageable));
            case WAITING:
                return convertBookings(bookingRepository.findByOwnerAndStatus(owner, BookingStatus.WAITING, pageable));
            case REJECTED:
                return convertBookings(bookingRepository.findByOwnerAndStatus(owner, BookingStatus.REJECTED, pageable));
        }

        throw new UnknownStateException("Unknown state: UNSUPPORTED_STATUS");
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