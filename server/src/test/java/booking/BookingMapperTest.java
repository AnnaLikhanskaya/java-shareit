package booking;

import org.junit.jupiter.api.Test;
import ru.practicum.booking.dto.BookingOutputDto;
import ru.practicum.booking.dto.DateBookingDto;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.booking.dto.BookingStatus.WAITING;

public class BookingMapperTest {
    private final User owner = User.builder().id(2L).build();
    private final ItemRequest request = ItemRequest.builder().id(1L).build();

    private final Booking booking = new Booking(
            1L,
            LocalDateTime.of(2024, 1, 1, 1, 1, 1),
            LocalDateTime.of(2024, 1, 2, 1, 1, 1),
            new Item(1L, "Корректный предмет", "Корректное описание", true, owner, request),
            new User(1L, "user", "user@gmail.com"),
            WAITING
    );

    @Test
    public void toBookingDtoTest() {
        BookingOutputDto bookingDto = BookingMapper.toBookingDto(booking);

        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    public void toDateBookingDtoTest() {
        DateBookingDto bookingDto = BookingMapper.toDateBookingDto(booking);

        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getBooker().getId(), bookingDto.getBookerId());
    }


    @Test
    public void toBookingTest() {
        UserDto itemOwner = new UserDto();
        itemOwner.setId(2L);
        itemOwner.setName("Owner Name");
        itemOwner.setEmail("owner@example.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item Name");
        itemDto.setDescription("Item Description");
        itemDto.setAvailable(true);
        itemDto.setOwner(itemOwner);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("User Name");
        userDto.setEmail("user@example.com");

        BookingOutputDto bookingDto = BookingOutputDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2024, 1, 1, 1, 1, 1))
                .end(LocalDateTime.of(2024, 1, 2, 1, 1, 1))
                .item(itemDto)
                .status(WAITING)
                .booker(userDto)
                .build();

        Booking newBooking = BookingMapper.toBooking(bookingDto, ItemRequest.builder().build());

        assertEquals(bookingDto.getId(), newBooking.getId());
        assertEquals(bookingDto.getStart(), newBooking.getStart());
        assertEquals(bookingDto.getEnd(), newBooking.getEnd());
        assertEquals(Item.builder()
                .name("Item Name")
                .available(true)
                .description("Item Description")
                .owner(User.builder()
                        .id(2L)
                        .email("owner@example.com")
                        .name("Owner Name")
                        .build())
                .id(1L)
                .request(ItemRequest.builder().build())
                .build(), newBooking.getItem());
        assertEquals(User.builder()
                .id(1L)
                .email("user@example.com")
                .name("User Name")
                .build(), newBooking.getBooker());
        assertEquals(bookingDto.getStatus(), newBooking.getStatus());
    }
}