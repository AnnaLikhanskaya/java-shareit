package ru.practicum.booking.mapper;

import ru.practicum.booking.dto.BookingOutputDto;
import ru.practicum.booking.dto.DateBookingDto;
import ru.practicum.booking.model.Booking;
import ru.practicum.request.model.ItemRequest;

import static ru.practicum.item.mapper.ItemMapper.toItem;
import static ru.practicum.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.user.mapper.UserMapper.toUser;
import static ru.practicum.user.mapper.UserMapper.toUserDto;

public class BookingMapper {
    public static BookingOutputDto toBookingDto(Booking booking) {
        return BookingOutputDto.builder()
                .id(booking.getId())
                .end(booking.getEnd())
                .start(booking.getStart())
                .booker(toUserDto(booking.getBooker()))
                .item(toItemDto(booking.getItem()))
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingOutputDto bookingDto, ItemRequest request) {
        return Booking.builder()
                .id(bookingDto.getId())
                .end(bookingDto.getEnd())
                .start(bookingDto.getStart())
                .booker(toUser(bookingDto.getBooker()))
                .item(toItem(bookingDto.getItem(), request))
                .status(bookingDto.getStatus())
                .build();
    }

    public static DateBookingDto toDateBookingDto(Booking booking) {
        return DateBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }
}