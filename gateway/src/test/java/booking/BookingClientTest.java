package booking;

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
import ru.practicum.booking.client.BookingClient;
import ru.practicum.booking.dto.BookItemRequestDto;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ShareItGateway.class, TestConfig.class})
public class BookingClientTest {

    @MockBean
    private RestTemplate restTemplate;
    private BookingClient bookingClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingClient = new BookingClient(restTemplate);
    }

    @Test
    void getAllBookings_ShouldReturnResponseEntity() {
        long userId = 1L;
        String state = "ALL";
        int from = 0;
        int size = 10;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(Object.class);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), anyMap())).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = bookingClient.getAllBookings(userId, state, from, size);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), anyMap());
    }

    @Test
    void createBooking_ShouldReturnResponseEntity() {
        long userId = 1L;
        BookItemRequestDto requestDto = new BookItemRequestDto();

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = bookingClient.createBooking(userId, requestDto);
        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class));
    }

    @Test
    void getBookingInfo_ShouldReturnResponseEntity() {
        long userId = 1L;
        long bookingId = 123L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = bookingClient.getBookingInfo(userId, bookingId);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class));
    }

    @Test
    void updateApprove_ShouldReturnResponseEntity() {
        long bookingId = 123L;
        boolean approved = true;
        long userId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = bookingClient.updateApprove(bookingId, approved, userId);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class));
    }

    @Test
    void getAllBookingsForOwner_ShouldReturnResponseEntity() {
        long userId = 1L;
        String state = "ALL";
        int from = 0;
        int size = 10;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), any(Map.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = bookingClient.getAllBookingsForOwner(userId, state, from, size);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), any(Map.class));
    }
}
