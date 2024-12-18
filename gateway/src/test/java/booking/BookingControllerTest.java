package booking;

import conf.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.ShareItGateway;
import ru.practicum.booking.client.BookingClient;
import ru.practicum.booking.controller.BookingController;
import ru.practicum.booking.dto.BookItemRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ShareItGateway.class, TestConfig.class})
public class BookingControllerTest {

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private BookingController bookingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateBooking() {
        BookItemRequestDto bookingInputDto = new BookItemRequestDto();
        Long userId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(bookingClient.createBooking(eq(userId), eq(bookingInputDto))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.createBooking(bookingInputDto, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testApproveBooking() {
        Long bookingId = 1L;
        Boolean approved = true;
        Long userId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(bookingClient.updateApprove(eq(bookingId), eq(approved), eq(userId))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.approve(bookingId, approved, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetBookingInfo() {
        Long bookingId = 1L;
        Long userId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(bookingClient.getBookingInfo(eq(userId), eq(bookingId))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getBookingInfo(bookingId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllBookings() {
        Long userId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 10;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(bookingClient.getAllBookings(eq(userId), eq(state), eq(from), eq(size))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getAllBookings(userId, state, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllBookingsForOwner() {
        Long userId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 10;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(bookingClient.getAllBookingsForOwner(eq(userId), eq(state), eq(from), eq(size))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getAllBookingsForOwner(userId, state, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}