package ru.practicum.user.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.client.UserClient;
import ru.practicum.user.dto.UserDto;
import ru.practicum.validationGroups.Create;
import ru.practicum.validationGroups.Update;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("Запрос на добавление пользователя {}", userDto);
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable Long userId,
            @Validated(Update.class) @RequestBody UserDto userDto
    ) {
        log.info("Запрос на обновление идентификатора пользователя = {}, новые данные: {}", userId, userDto);
        return userClient.updateUser(userId, userDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Запрос на список пользователей");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        log.info("Запрос пользователя по Id = {}", userId);
        return userClient.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable long userId) {
        log.info("Запрос на удаление пользователя = {}", userId);
        return userClient.deleteUserById(userId);
    }
}