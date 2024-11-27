package user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.user.controller.UserController;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetUsers() {
        List<UserDto> expectedUsers = Arrays.asList(new UserDto(), new UserDto());

        when(userService.getUsers()).thenReturn(expectedUsers);

        List<UserDto> actualUsers = userController.getUsers();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        UserDto expectedUser = new UserDto();

        when(userService.getUserById(eq(userId))).thenReturn(expectedUser);

        UserDto actualUser = userController.getUserById(userId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testCreateUser() {
        UserDto userDto = new UserDto();
        UserDto expectedUser = new UserDto();

        when(userService.createUser(eq(userDto))).thenReturn(expectedUser);

        UserDto actualUser = userController.createUser(userDto);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        UserDto expectedUser = new UserDto();

        when(userService.updateUser(eq(userDto), eq(userId))).thenReturn(expectedUser);

        UserDto actualUser = userController.updateUser(userId, userDto);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(eq(userId));

        userController.deleteUser(userId);

        // No assertions needed as the method has a void return type
    }
}