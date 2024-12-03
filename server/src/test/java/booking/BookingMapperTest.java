package booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServer;
import ru.practicum.booking.dto.BookingStatus;
import ru.practicum.booking.dto.DateBookingDto;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest
public class BookingMapperTest {

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToDateBookingDto() {
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 12, 3, 10, 0))
                .end(LocalDateTime.of(2023, 12, 3, 12, 0))
                .booker(User.builder().id(1L).name("Booker").build())
                .item(Item.builder().build())
                .status(BookingStatus.APPROVED)
                .build();

        DateBookingDto dateBookingDto = BookingMapper.toDateBookingDto(booking);

        assertEquals(1L, dateBookingDto.getId());
        assertEquals(1L, dateBookingDto.getBookerId());
        assertEquals(LocalDateTime.of(2023, 12, 3, 10, 0), dateBookingDto.getStart());
        assertEquals(LocalDateTime.of(2023, 12, 3, 12, 0), dateBookingDto.getEnd());
    }
}