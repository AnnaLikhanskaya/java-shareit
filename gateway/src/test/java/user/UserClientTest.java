package user;

import conf.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ShareItGateway;
import ru.practicum.user.client.UserClient;
import ru.practicum.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ShareItGateway.class, TestConfig.class})
public class UserClientTest {

    @MockBean
    private RestTemplate restTemplate;
    private UserClient userClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userClient = new UserClient(restTemplate);
    }

    @Test
    void createUser_ShouldReturnResponseEntity() {
        UserDto userDto = new UserDto();

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userClient.createUser(userDto);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class));
    }

    @Test
    void updateUser_ShouldReturnResponseEntity() {
        long userId = 1L;
        UserDto userDto = new UserDto();

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userClient.updateUser(userId, userDto);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class));
    }

    @Test
    void getAllUsers_ShouldReturnResponseEntity() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userClient.getAllUsers();

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class));
    }

    @Test
    void getUserById_ShouldReturnResponseEntity() {
        long userId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userClient.getUserById(userId);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class));
    }

    @Test
    void deleteUserById_ShouldReturnResponseEntity() {
        long userId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = userClient.deleteUserById(userId);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(Object.class));
    }
}