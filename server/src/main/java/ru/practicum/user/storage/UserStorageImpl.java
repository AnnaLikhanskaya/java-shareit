package ru.practicum.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.NotExsistObject;
import ru.practicum.user.model.User;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@Slf4j
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, User> usersEmail = new HashMap<>();
    private int id = 1;

    @Override
    public List<User> getUsers() {
        log.info("Получен запрос на вывод всех пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        User user = checkUserAvailability("найти", id);
        log.info("Получен запрос на вывод пользователя по id");
        return user;
    }

    @Override
    public User addUser(User user) {
        isExist(user.getEmail());
        user.setId(getId());
        users.put(user.getId(), user);
        usersEmail.put(user.getEmail(), user);
        log.info("Получен запрос на добавление пользователя");
        return user;
    }

    @Override
    public User updateUser(User user) {
        User exsistingsUser = checkUserAvailability("изменить", user.getId());
        String email = exsistingsUser.getEmail();
        if (user.getName() != null && !user.getName().isBlank()) {
            exsistingsUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            isExist(user.getEmail());
            exsistingsUser.setEmail(user.getEmail());
            usersEmail.remove(email);
            usersEmail.put(exsistingsUser.getEmail(), exsistingsUser);
        }
        log.info("Получен запрос на изменение пользователя");
        return exsistingsUser;
    }

    @Override
    public void deleteUser(Long id) {
        checkUserAvailability("найти", id);
        usersEmail.remove(users.get(id).getEmail());
        users.remove(id);
        log.info("Получен запрос на удаление пользователя");
    }

    @Override
    public User checkUserAvailability(String operation, Long id) {
        User user = users.get(id);
        if (!users.containsKey(id)) {
            String massage = String.format("Невозможно %s. Пользователь отсутствует!", operation);
            throw new NotExsistObject(massage);
        }
        return user;

    }

    private void isExist(String email) {
        if (usersEmail.containsKey(email)) {
            throw new DuplicateException("Пользователь с таким email уже существует");
        }
    }

    private long getId() {
        return id++;
    }
}