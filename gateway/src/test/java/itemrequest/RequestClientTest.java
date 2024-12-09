package itemrequest;

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
import ru.practicum.request.client.RequestClient;
import ru.practicum.request.dto.ItemRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ShareItGateway.class, TestConfig.class})
public class RequestClientTest {

    @MockBean
    private RestTemplate restTemplate;
    private RequestClient requestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestClient = new RequestClient(restTemplate);
    }

    @Test
    void addRequest_ShouldReturnResponseEntity() {
        long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = requestClient.addRequest(itemRequestDto, userId);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class));
    }

    @Test
    void getRequestsByUser_ShouldReturnResponseEntity() {
        long userId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = requestClient.getRequestsByUser(userId);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class));
    }

    @Test
    void getAllRequests_ShouldReturnResponseEntity() {
        long userId = 1L;
        int from = 0;
        int size = 10;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), anyMap())).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = requestClient.getAllRequests(userId, from, size);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), anyMap());
    }

    @Test
    void getRequestById_ShouldReturnResponseEntity() {
        long itemRequestId = 1L;
        long userId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = requestClient.getRequestById(itemRequestId, userId);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class));
    }
}