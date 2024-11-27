package booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.booking.dto.BookingOutputDto;
import ru.practicum.booking.dto.BookingStatus;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BookingOutputDtoTest {

    @Test
    void testBuilder() {
        Long id = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        ItemDto itemDto = new ItemDto();
        UserDto userDto = new UserDto();
        BookingStatus status = BookingStatus.APPROVED;

        BookingOutputDto bookingOutputDto = BookingOutputDto.builder()
                .id(id)
                .start(start)
                .end(end)
                .item(itemDto)
                .booker(userDto)
                .status(status)
                .build();

        assertThat(bookingOutputDto.getId()).isEqualTo(id);
        assertThat(bookingOutputDto.getStart()).isEqualTo(start);
        assertThat(bookingOutputDto.getEnd()).isEqualTo(end);
        assertThat(bookingOutputDto.getItem()).isEqualTo(itemDto);
        assertThat(bookingOutputDto.getBooker()).isEqualTo(userDto);
        assertThat(bookingOutputDto.getStatus()).isEqualTo(status);
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        ItemDto itemDto = new ItemDto();
        UserDto userDto = new UserDto();
        BookingStatus status = BookingStatus.APPROVED;

        BookingOutputDto bookingOutputDto = new BookingOutputDto(id, start, end, itemDto, userDto, status);

        assertThat(bookingOutputDto.getId()).isEqualTo(id);
        assertThat(bookingOutputDto.getStart()).isEqualTo(start);
        assertThat(bookingOutputDto.getEnd()).isEqualTo(end);
        assertThat(bookingOutputDto.getItem()).isEqualTo(itemDto);
        assertThat(bookingOutputDto.getBooker()).isEqualTo(userDto);
        assertThat(bookingOutputDto.getStatus()).isEqualTo(status);
    }

    @Test
    void testNoArgsConstructor() {
        BookingOutputDto bookingOutputDto = new BookingOutputDto(null, null, null, null, null, null);

        assertThat(bookingOutputDto.getId()).isNull();
        assertThat(bookingOutputDto.getStart()).isNull();
        assertThat(bookingOutputDto.getEnd()).isNull();
        assertThat(bookingOutputDto.getItem()).isNull();
        assertThat(bookingOutputDto.getBooker()).isNull();
        assertThat(bookingOutputDto.getStatus()).isNull();
    }
}