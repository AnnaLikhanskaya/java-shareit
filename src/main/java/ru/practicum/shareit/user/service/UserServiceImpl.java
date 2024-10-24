package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(@Qualifier("userStorageImpl") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(userStorage.getUserById(id));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        userStorage.addUser(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        userDto.setId(id);
        User user = UserMapper.toUser(userDto);
        userStorage.updateUser(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }
}