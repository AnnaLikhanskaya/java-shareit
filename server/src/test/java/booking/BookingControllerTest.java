package booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.ShareItServer;
import ru.practicum.booking.controller.BookingController;
import ru.practicum.booking.dto.BookingOutputDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.booking.dto.BookingStatus;
import ru.practicum.booking.service.BookingService;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest
public class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    public void testCreateBooking() throws Exception {
        List<MockHttpServletRequestBuilder> needIdUserFromHeadRequest =
                List.of(get("/bookings/1"),
                        get("/bookings"),
                        get("/bookings/owner"),
                        post("/bookings"),
                        patch("/bookings/1?approved=true"));

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();

        for (var requestBuilder : needIdUserFromHeadRequest) {
            var result = mockMvc.perform(requestBuilder)
                    .andReturn();

            var status = result
                    .getResponse()
                    .getStatus();

            var path = result.getRequest().getMethod() + " '" + result.getRequest().getRequestURI() + "'";

            Assertions.assertEquals(400, status, path + " was needed userid as RequestHeader \"X-Sharer-User-Id\"");
        }
    }


    @Test
    public void testApproveBooking() {
        Long bookingId = 1L;
        Boolean approved = true;
        Long userId = 1L;
        BookingOutputDto expectedResponse = new BookingOutputDto(1L, LocalDateTime.now(), LocalDateTime.now(),
                ItemDto.builder().build(),
                new UserDto(), BookingStatus.APPROVED);

        when(bookingService.updateApprove(eq(bookingId), eq(approved), eq(userId))).thenReturn(expectedResponse);

        BookingOutputDto response = bookingController.approve(bookingId, approved, userId);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetBookingInfo() {
        Long bookingId = 1L;
        Long userId = 1L;
        BookingOutputDto expectedResponse = new BookingOutputDto(1L, LocalDateTime.now(), LocalDateTime.now(),
                ItemDto.builder().build(),
                new UserDto(), BookingStatus.APPROVED);

        when(bookingService.getBookingInfo(eq(bookingId), eq(userId))).thenReturn(expectedResponse);

        BookingOutputDto response = bookingController.getBookingInfo(bookingId, userId);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetAllBookings() {
        Long userId = 1L;
        BookingState state = BookingState.ALL;
        Integer from = 0;
        Integer size = 10;
        List<BookingOutputDto> expectedResponse = List.of(new BookingOutputDto(1L, LocalDateTime.now(),
                LocalDateTime.now(),
                ItemDto.builder().build(),
                new UserDto(), BookingStatus.APPROVED));

        when(bookingService.getAllBookings(eq(userId), eq(state), eq(from), eq(size))).thenReturn(expectedResponse);

        List<BookingOutputDto> response = bookingController.getAllBookings(userId, state, from, size);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetAllBookingsForOwner() {
        Long userId = 1L;
        BookingState state = BookingState.ALL;
        Integer from = 0;
        Integer size = 10;
        List<BookingOutputDto> expectedResponse = List.of(new BookingOutputDto(1L,
                LocalDateTime.now(), LocalDateTime.now(),
                ItemDto.builder().build(),
                new UserDto(), BookingStatus.APPROVED));

        when(bookingService.getAllBookingsForOwner(eq(userId), eq(state), eq(from), eq(size))).thenReturn(expectedResponse);

        List<BookingOutputDto> response = bookingController.getAllBookingsForOwner(userId, state, from, size);

        assertEquals(expectedResponse, response);
    }
}