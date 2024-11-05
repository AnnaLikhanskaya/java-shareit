package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validationGroups.Create;
import ru.practicum.shareit.validationGroups.Update;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя не может быть пустым", groups = {Create.class})
    private String name;
    @Email(message = "Некорректный email", groups = {Create.class, Update.class})
    @NotBlank(message = "email не может быть пустым", groups = {Create.class})
    private String email;
}