//package user;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//import ru.practicum.user.client.UserClient;
//import ru.practicum.user.dto.UserDto;
//
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UserClientTest {
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @InjectMocks
//    private UserClient userClient;
//
//    @Test
//    void testCreateUser() {
//        UserDto userDto = new UserDto();
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = userClient.createUser(userDto);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testUpdateUser() {
//        UserDto userDto = new UserDto();
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = userClient.updateUser(1L, userDto);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testGetAllUsers() {
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = userClient.getAllUsers();
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testGetUserById() {
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = userClient.getUserById(1L);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testDeleteUserById() {
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = userClient.deleteUserById(1L);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//}
