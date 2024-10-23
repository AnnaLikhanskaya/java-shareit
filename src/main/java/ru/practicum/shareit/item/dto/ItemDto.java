package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validationGroups.Create;

/**
 * TODO Sprint add-controllers.
 */

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

    private Long owner;
}