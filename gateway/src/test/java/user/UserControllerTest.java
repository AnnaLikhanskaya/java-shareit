package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.user.client.UserClient;
import ru.practicum.user.controller.UserController;
import ru.practicum.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUser() {
        UserDto userDto = new UserDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(userClient.createUser(eq(userDto))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.createUser(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(userClient.updateUser(eq(userId), eq(userDto))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.updateUser(userId, userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllUsers() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(userClient.getAllUsers()).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(userClient.getUserById(eq(userId))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUserById() {
        Long userId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(userClient.deleteUserById(eq(userId))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.deleteUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}