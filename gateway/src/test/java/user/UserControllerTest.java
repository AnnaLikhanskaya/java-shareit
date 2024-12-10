package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.user.client.UserClient;
import ru.practicum.user.controller.UserController;
import ru.practicum.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = ShareItGateway.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController userController;

    private UserDto userDto;
    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        userDto = new UserDto();
    }

    @Test
    void createUser_shouldCreateUser() {
        when(userClient.createUser(userDto)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = userController.createUser(userDto);

        verify(userClient, times(1)).createUser(userDto);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void updateUser_shouldUpdateUser() {
        when(userClient.updateUser(userId, userDto)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = userController.updateUser(userId, userDto);

        verify(userClient, times(1)).updateUser(userId, userDto);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        when(userClient.getAllUsers()).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = userController.getAllUsers();

        verify(userClient, times(1)).getAllUsers();
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(userClient.getUserById(userId)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = userController.getUserById(userId);

        verify(userClient, times(1)).getUserById(userId);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void deleteUserById_shouldDeleteUser() {
        when(userClient.deleteUserById(userId)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = userController.deleteUserById(userId);

        verify(userClient, times(1)).deleteUserById(userId);
        assertEquals(ResponseEntity.ok().build(), response);
    }
}