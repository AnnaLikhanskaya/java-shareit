package ru.practicum.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.booking.dto.DateBookingDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.validationGroups.Create;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    @NotBlank(message = "Название не может быть пустым", groups = Create.class)
    private String name;
    @NotBlank(message = "Описание не может быть пустым", groups = Create.class)
    private String description;
    @NotNull(message = "Статус не может отсутствовать", groups = Create.class)
    private Boolean available;
    private UserDto owner;
    private List<CommentDto> comments;
    private DateBookingDto lastBooking;
    private DateBookingDto nextBooking;
    private Long requestId;
}