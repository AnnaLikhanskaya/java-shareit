package booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.booking.dto.BookingOutputDto;
import ru.practicum.booking.dto.BookingStatus;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingMapperTest {

    @Mock
    private ItemRequest itemRequest;

    @InjectMocks
    private BookingMapper bookingMapper;

    private BookingOutputDto bookingDto;
    private Item item;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        item = new Item();
        item.setId(1L);
        item.setName("Item Name");
        item.setDescription("Item Description");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDto = BookingOutputDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2024, 1, 1, 1, 1, 1))
                .end(LocalDateTime.of(2024, 1, 2, 1, 1, 1))
                .item(ItemDto.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .available(item.getAvailable())
                        .owner(UserDto.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .build())
                        .build())
                .booker(UserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build())
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    public void toBookingTest() {
        Booking newBooking = BookingMapper.toBooking(bookingDto, itemRequest);

        assertEquals(bookingDto.getId(), newBooking.getId());
        assertEquals(bookingDto.getStart(), newBooking.getStart());
        assertEquals(bookingDto.getEnd(), newBooking.getEnd());
        assertEquals(bookingDto.getItem().getId(), newBooking.getItem().getId());
        assertEquals(bookingDto.getBooker().getId(), newBooking.getBooker().getId());
        assertEquals(bookingDto.getStatus(), newBooking.getStatus());
    }
}