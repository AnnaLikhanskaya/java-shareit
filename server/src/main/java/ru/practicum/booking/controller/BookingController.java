package ru.practicum.booking.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingInputDto;
import ru.practicum.booking.dto.BookingOutputDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.booking.service.BookingService;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingOutputDto createBooking(
            @RequestBody BookingInputDto bookingInputDto,
            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId
    ) {
        return bookingService.createBooking(bookingInputDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto approve(
            @PathVariable Long bookingId,
            @RequestParam @NotNull Boolean approved,
            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId
    ) {
        return bookingService.updateApprove(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto getBookingInfo(@PathVariable Long bookingId,
                                           @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return bookingService.getBookingInfo(bookingId, userId);
    }

    @GetMapping
    public List<BookingOutputDto> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
            @RequestParam(defaultValue = "ALL", required = false) BookingState state,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size
    ) {
        return bookingService.getAllBookings(userId, state, from, size);
    }

    @GetMapping("owner")
    public List<BookingOutputDto> getAllBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
            @RequestParam(defaultValue = "ALL", required = false) BookingState state,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size
    ) {
        return bookingService.getAllBookingsForOwner(userId, state, from, size);
    }
}