package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.NotExsistObject;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserServiceDb;
import ru.practicum.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceDbTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceDb userServiceDb;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Имя Фамилия");
        user.setEmail("name.111@example.com");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Имя Фамилия");
        userDto.setEmail("name.111@example.com");
    }

    @Test
    void getUsers_notFound() {
        when(userRepository.findById(any())).thenThrow(new UserNotFoundException(any()));

        assertThrows(UserNotFoundException.class, () -> userServiceDb.getUserById(1L));
    }

    @Test
    void getUsers_shouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = userServiceDb.getUsers();
        verify(userRepository, atMost(1)).findAll();

        assertEquals(1, result.size());
        assertEquals(userDto, result.get(0));
    }

    @Test
    void getUserById_shouldReturnUserDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userServiceDb.getUserById(1L);

        assertEquals(userDto, result);
    }

    @Test
    void getUserById_shouldThrowNotExsistObject() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotExsistObject.class, () -> userServiceDb.getUserById(1L));
    }

    @Test
    void createUser_shouldReturnUserDto() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userServiceDb.createUser(userDto);

        assertEquals(userDto, result);
    }

    @Test
    void createUser_shouldThrowDuplicateException() {
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Duplicate email"));

        assertThrows(DuplicateException.class, () -> userServiceDb.createUser(userDto));
    }

    @Test
    void updateUser_shouldReturnUserDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setName("Jane Doe");
        updatedUserDto.setEmail("jane.doe@example.com");

        UserDto result = userServiceDb.updateUser(updatedUserDto, 1L);

        assertEquals(updatedUserDto.getName(), result.getName());
        assertEquals(updatedUserDto.getEmail(), result.getEmail());
    }

    @Test
    void updateUser_shouldThrowNotExsistObject() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotExsistObject.class, () -> userServiceDb.updateUser(userDto, 1L));
    }

    @Test
    void updateUser_shouldThrowDuplicateException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Duplicate email"));

        assertThrows(DuplicateException.class, () -> userServiceDb.updateUser(userDto, 1L));
    }

    @Test
    void deleteUser_shouldCallRepositoryDelete() {
        userServiceDb.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
