//package itemrequest;
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
//import ru.practicum.request.client.RequestClient;
//import ru.practicum.request.dto.ItemRequestDto;
//
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class RequestClientTest {
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @InjectMocks
//    private RequestClient requestClient;
//
//    @Test
//    void testAddRequest() {
//        ItemRequestDto itemRequestDto = new ItemRequestDto();
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = requestClient.addRequest(itemRequestDto, 1L);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testGetRequestsByUser() {
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = requestClient.getRequestsByUser(1L);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testGetAllRequests() {
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = requestClient.getAllRequests(1L, 0, 10);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testGetRequestById() {
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = requestClient.getRequestById(1L, 1L);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//}