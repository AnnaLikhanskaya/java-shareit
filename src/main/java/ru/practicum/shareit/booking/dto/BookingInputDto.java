package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingInputDto {
    @NotNull
    private Long itemId;
    @NotNull(message = "Начальная дата не может быть нулевой")
    @Future(message = "Начало бронирования не может быть в прошлом")
    private LocalDateTime start;
    @NotNull(message = "Конечная дата не может быть нулевой")
    @Future(message = "Конец бронирования не может быть в прошлом")
    private LocalDateTime end;
}