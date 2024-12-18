package ru.practicum.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemForRequestDto {
    private Long id;
    String name;
    @NotBlank(message = "Обьект без описания")
    private String description;
    private Boolean available;
    private Long requestId;
}
