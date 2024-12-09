package ru.practicum.user.storage;


import ru.practicum.user.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User getUserById(Long id);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    User checkUserAvailability(String operation, Long id);
}
