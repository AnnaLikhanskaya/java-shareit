package booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.booking.comparator.BookingComparator;
import ru.practicum.booking.dto.BookingOutputDto;
import ru.practicum.booking.dto.BookingStatus;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingComparatorTest {
    private BookingComparator bookingComparator;
    private BookingOutputDto booking1;
    private BookingOutputDto booking2;

    @BeforeEach
    public void setUp() {
        bookingComparator = new BookingComparator();
        booking1 = BookingOutputDto.builder()
                .id(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(ItemDto.builder().build())
                .booker(UserDto.builder().build())
                .status(BookingStatus.WAITING).build();

        booking2 = BookingOutputDto.builder()
                .id(2L)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().plusDays(2))
                .item(ItemDto.builder().build())
                .booker(UserDto.builder().build())
                .status(BookingStatus.WAITING).build();
    }

    @Test
    public void testCompare_EqualStartTimes() {
        LocalDateTime startTime = LocalDateTime.of(2023, 10, 1, 10, 0);
        booking1.setStart(startTime);
        booking2.setStart(startTime);

        int result = bookingComparator.compare(booking1, booking2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstStartTimeBeforeSecond() {
        LocalDateTime startTime1 = LocalDateTime.of(2023, 10, 1, 10, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2023, 10, 1, 12, 0);
        booking1.setStart(startTime1);
        booking2.setStart(startTime2);

        int result = bookingComparator.compare(booking1, booking2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstStartTimeAfterSecond() {
        LocalDateTime startTime1 = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2023, 10, 1, 10, 0);
        booking1.setStart(startTime1);
        booking2.setStart(startTime2);

        int result = bookingComparator.compare(booking1, booking2);
        assertEquals(1, result);
    }

}
