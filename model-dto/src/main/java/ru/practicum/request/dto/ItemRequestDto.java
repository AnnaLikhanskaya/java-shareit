package ru.practicum.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.dto.ItemForRequestDto;

import java.time.LocalDateTime;
import java.util.List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class ItemRequestDto {
        private Long id;
        @NotBlank(message = "Отсутствует описание обьекта, которое вы ищите")
        private String description;
        private LocalDateTime created;
        private List<ItemForRequestDto> items;
    }