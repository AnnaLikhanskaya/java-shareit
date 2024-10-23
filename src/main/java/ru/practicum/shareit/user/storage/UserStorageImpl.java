package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotExsistObject;
import ru.practicum.shareit.user.model.User;

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
        checkUserAvailability("Найти", id);
        log.info("Получен запрос на вывод пользователя по id");
        return users.get(id);
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
        checkUserAvailability("Изменить", user.getId());
        User updateUser = users.get(user.getId());
        String email = updateUser.getEmail();
        User updateEmail = usersEmail.get(email);
        if (user.getName() != null && !user.getName().isBlank()) {
            updateUser.setName(user.getName());
            updateEmail.setName(user.getName());
        }
        if (user.getEmail() != null) {
            isExist(user.getEmail());
            updateUser.setEmail(user.getEmail());
            usersEmail.remove(email);
            usersEmail.put(updateUser.getEmail(), updateUser);
        }
        log.info("Получен запрос на изменение пользователя");
        return updateUser;
    }

    @Override
    public void deleteUser(Long id) {
        checkUserAvailability("Удалить", id);
        usersEmail.remove(users.get(id).getEmail());
        users.remove(id);
        log.info("Получен запрос на удаление пользователя");
    }

    @Override
    public void checkUserAvailability(String operation, Long id) {
        String massage = String.format("Alarm %s. Пользователь отсутствует!", operation);
        if (!users.containsKey(id)) {
            throw new NotExsistObject(massage);
        }
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