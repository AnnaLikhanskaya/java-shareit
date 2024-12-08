package booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.ShareItServer;
import ru.practicum.booking.dto.BookingOutputDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.booking.dto.BookingStatus;
import ru.practicum.booking.service.BookingService;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingOutputDto createBookingOutputDto(Long id) {
        return BookingOutputDto.builder()
                .id(id)
                .start(LocalDateTime.now().plusMinutes(id))
                .end(LocalDateTime.now().plusHours(id))
                .item(new ItemDto())
                .booker(new UserDto())
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void createBookingTest() throws Exception {
        BookingOutputDto returnBookingDto = createBookingOutputDto(1L);

        when(bookingService.createBooking(any(), any())).thenReturn(returnBookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(createBookingOutputDto(1L)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(returnBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item", Matchers.notNullValue()))
                .andExpect(jsonPath("$.booker", Matchers.notNullValue()))
                .andExpect(jsonPath("$.status", Matchers.is(returnBookingDto.getStatus().name())))
                .andExpect(jsonPath("$.start", Matchers.containsString(
                        returnBookingDto.getStart().toString().substring(0, 24))))
                .andExpect(jsonPath("$.end", Matchers.containsString(
                        returnBookingDto.getEnd().toString().substring(0, 24))));

        verify(bookingService, times(1)).createBooking(any(), any());
    }

    @Test
    void testApproveBooking() throws Exception {
        BookingOutputDto bookingOutputDto = createBookingOutputDto(1L);
        bookingOutputDto.setStatus(BookingStatus.APPROVED);

        when(bookingService.updateApprove(any(), any(), any())).thenReturn(bookingOutputDto);

        mockMvc.perform(patch("/bookings/{id}?approved=true", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(bookingOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.item", Matchers.notNullValue()))
                .andExpect(jsonPath("$.booker", Matchers.notNullValue()))
                .andExpect(jsonPath("$.status", Matchers.is(bookingOutputDto.getStatus().name())))
                .andExpect(jsonPath("$.start", Matchers.containsString(
                        bookingOutputDto.getStart().toString().substring(0, 24))))
                .andExpect(jsonPath("$.end", Matchers.containsString(
                        bookingOutputDto.getEnd().toString().substring(0, 24))));

        verify(bookingService, times(1)).updateApprove(any(), any(), any());
    }

    @Test
    void testGetAllBookingsForOwner() throws Exception {
        List<BookingOutputDto> returnBookingDtoList = List.of(createBookingOutputDto(1L), createBookingOutputDto(2L));

        when(bookingService.getAllBookingsForOwner(anyLong(), any(BookingState.class), any(), any()))
                .thenReturn(returnBookingDtoList);

        ResultActions resultActions =
                mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(returnBookingDtoList.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[1].id", Matchers.is(returnBookingDtoList.get(1).getId()), Long.class))
                .andExpect(jsonPath("$[0].start", Matchers.containsString(
                        returnBookingDtoList.get(0).getStart().toString().substring(0, 24))))
                .andExpect(jsonPath("$[0].end", Matchers.containsString(
                        returnBookingDtoList.get(0).getEnd().toString().substring(0, 24))))
                .andExpect(jsonPath("$[1].start", Matchers.containsString(
                        returnBookingDtoList.get(1).getStart().toString().substring(0, 24))))
                .andExpect(jsonPath("$[1].end", Matchers.containsString(
                        returnBookingDtoList.get(1).getEnd().toString().substring(0, 24))));

        verify(bookingService, times(1)).getAllBookingsForOwner(anyLong(), any(BookingState.class), any(), any());
    }

    @Test
    void testGetBookingInfo() throws Exception {
        Long bookingId = 1L;
        Long userId = 1L;
        BookingOutputDto expectedResponse = createBookingOutputDto(bookingId);
        expectedResponse.setStatus(BookingStatus.APPROVED);

        when(bookingService.getBookingInfo(anyLong(), anyLong())).thenReturn(expectedResponse);

        mockMvc.perform(get("/bookings/{id}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(expectedResponse.getId()), Long.class))
                .andExpect(jsonPath("$.item", Matchers.notNullValue()))
                .andExpect(jsonPath("$.booker", Matchers.notNullValue()))
                .andExpect(jsonPath("$.status", Matchers.is(expectedResponse.getStatus().name())))
                .andExpect(jsonPath("$.start", Matchers.containsString(
                        expectedResponse.getStart().toString().substring(0, 24))))
                .andExpect(jsonPath("$.end", Matchers.containsString(
                        expectedResponse.getEnd().toString().substring(0, 24))));

        verify(bookingService, times(1)).getBookingInfo(anyLong(), anyLong());
    }

    @Test
    void testGetAllBookings() throws Exception {
        Long userId = 1L;
        BookingState state = BookingState.ALL;
        Integer from = 0;
        Integer size = 10;
        List<BookingOutputDto> expectedResponse = List.of(createBookingOutputDto(1L));

        when(bookingService.getAllBookings(anyLong(), any(BookingState.class), anyInt(), anyInt()))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state.name())
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(expectedResponse.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].start", Matchers.containsString(
                        expectedResponse.get(0).getStart().toString().substring(0, 24))))
                .andExpect(jsonPath("$[0].end", Matchers.containsString(
                        expectedResponse.get(0).getEnd().toString().substring(0, 24))))
                .andExpect(jsonPath("$[0].item", Matchers.notNullValue()))
                .andExpect(jsonPath("$[0].booker", Matchers.notNullValue()))
                .andExpect(jsonPath("$[0].status", Matchers.is(expectedResponse.get(0).getStatus().name())));

        verify(bookingService, times(1)).getAllBookings(anyLong(), any(BookingState.class), anyInt(), anyInt());
    }
}