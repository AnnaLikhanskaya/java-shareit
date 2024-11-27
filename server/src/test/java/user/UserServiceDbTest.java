package user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.NotExsistObject;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserServiceDb;
import ru.practicum.user.storage.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceDbTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceDb userService;

    @Test
    public void testGetUsers() {
        List<User> users = Arrays.asList(
                User.builder().id(1L).name("User 1").email("user1@example.com").build(),
                User.builder().id(2L).name("User 2").email("user2@example.com").build()
        );

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> userDtos = userService.getUsers();

        assertEquals(2, userDtos.size());
        assertEquals(users.get(0).getId(), userDtos.get(0).getId());
        assertEquals(users.get(1).getId(), userDtos.get(1).getId());
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = User.builder().id(userId).name("User 1").email("user1@example.com").build();

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(userId);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    public void testGetUserByIdNotFound() {
        Long userId = 1L;

        when(userRepository.findById(eq(userId))).thenReturn(Optional.empty());

        assertThrows(NotExsistObject.class, () -> userService.getUserById(userId));
    }

    @Test
    public void testCreateUser() {
        UserDto userDto = UserDto.builder().name("New User").email("newuser@example.com").build();
        User user = UserMapper.toUser(userDto);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto createdUserDto = userService.createUser(userDto);

        assertEquals(user.getId(), createdUserDto.getId());
        assertEquals(user.getName(), createdUserDto.getName());
        assertEquals(user.getEmail(), createdUserDto.getEmail());
    }

    @Test
    public void testCreateUserDuplicate() {
        UserDto userDto = UserDto.builder().name("New User").email("newuser@example.com").build();

        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Duplicate email"));

        assertThrows(DuplicateException.class, () -> userService.createUser(userDto));
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        UserDto userDto = UserDto.builder().name("Updated User").email("updateduser@example.com").build();
        User user = User.builder().id(userId).name("User 1").email("user1@example.com").build();

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto updatedUserDto = userService.updateUser(userDto, userId);

        assertEquals(user.getId(), updatedUserDto.getId());
        assertEquals(userDto.getName(), updatedUserDto.getName());
        assertEquals(userDto.getEmail(), updatedUserDto.getEmail());
    }

    @Test
    public void testUpdateUserNotFound() {
        Long userId = 1L;
        UserDto userDto = UserDto.builder().name("Updated User").email("updateduser@example.com").build();

        when(userRepository.findById(eq(userId))).thenReturn(Optional.empty());

        assertThrows(NotExsistObject.class, () -> userService.updateUser(userDto, userId));
    }

    @Test
    public void testUpdateUserDuplicate() {
        Long userId = 1L;
        UserDto userDto = UserDto.builder().name("Updated User").email("updateduser@example.com").build();
        User user = User.builder().id(userId).name("User 1").email("user1@example.com").build();

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Duplicate email"));

        assertThrows(DuplicateException.class, () -> userService.updateUser(userDto, userId));
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(eq(userId));

        userService.deleteUser(userId);

        // No assertions needed as the method has a void return type
    }
}
