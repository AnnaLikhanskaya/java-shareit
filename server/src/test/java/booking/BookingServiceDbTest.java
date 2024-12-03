package booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServer;
import ru.practicum.booking.dto.BookingInputDto;
import ru.practicum.booking.dto.BookingStatus;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.service.BookingServiceDb;
import ru.practicum.booking.storage.BookingRepository;
import ru.practicum.exception.InvalidRequestException;
import ru.practicum.exception.NotExsistObject;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.model.Item;
import ru.practicum.item.storage.ItemRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest
public class BookingServiceDbTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceDb bookingServiceDb;

    private Booking booking;
    private User user;
    private Item item;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 12, 3, 10, 0))
                .end(LocalDateTime.of(2023, 12, 3, 12, 0))
                .booker(User.builder().id(1L).name("Booker").build())
                .item(Item.builder().id(2L).name("Item").available(true).owner(User.builder().id(3L).build()).build())
                .status(BookingStatus.WAITING)
                .build();

        user = User.builder()
                .id(1L)
                .email("user@example.com")
                .name("User")
                .build();

        item = Item.builder()
                .id(2L)
                .name("Item")
                .available(true)
                .owner(User.builder().id(3L).build())
                .build();
    }


    @Test
    public void testCreateBooking_InvalidTime() {
        BookingInputDto bookingInputDto = BookingInputDto.builder()
                .start(LocalDateTime.of(2023, 12, 3, 12, 0))
                .end(LocalDateTime.of(2023, 12, 3, 10, 0))
                .itemId(2L)
                .build();

        assertThrows(InvalidRequestException.class, () -> bookingServiceDb.createBooking(bookingInputDto, 1L));
    }

    @Test
    public void testCreateBooking_ItemNotAvailable() {
        item.setAvailable(false);
        BookingInputDto bookingInputDto = BookingInputDto.builder()
                .start(LocalDateTime.of(2023, 12, 3, 10, 0))
                .end(LocalDateTime.of(2023, 12, 3, 12, 0))
                .itemId(2L)
                .build();

        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(itemRepository.findById(eq(2L))).thenReturn(Optional.of(item));

        assertThrows(UserNotFoundException.class, () -> bookingServiceDb.createBooking(bookingInputDto, 1L));
    }

    @Test
    public void testCreateBooking_UserOwnsItem() {
        item.getOwner().setId(1L);
        BookingInputDto bookingInputDto = BookingInputDto.builder()
                .start(LocalDateTime.of(2023, 12, 3, 10, 0))
                .end(LocalDateTime.of(2023, 12, 3, 12, 0))
                .itemId(2L)
                .build();

        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(itemRepository.findById(eq(2L))).thenReturn(Optional.of(item));

        assertThrows(UserNotFoundException.class, () -> bookingServiceDb.createBooking(bookingInputDto, 1L));
    }

    @Test
    public void testUpdateApprove_NotOwner() {
        when(bookingRepository.findById(eq(1L))).thenReturn(Optional.of(booking));
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));

        assertThrows(NotExsistObject.class, () -> bookingServiceDb.updateApprove(1L, true, 1L));
    }

    @Test
    public void testUpdateApprove_NotWaiting() {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(eq(1L))).thenReturn(Optional.of(booking));
        when(userRepository.findById(eq(3L))).thenReturn(Optional.of(item.getOwner()));

        assertThrows(NotExsistObject.class, () -> bookingServiceDb.updateApprove(1L, true, 3L));
    }

    @Test
    public void testGetBookingInfo_NotAuthorized() {
        when(userRepository.findById(eq(2L))).thenReturn(Optional.of(User.builder().id(2L).build()));
        when(bookingRepository.findById(eq(1L))).thenReturn(Optional.of(booking));

        assertThrows(UserNotFoundException.class, () -> bookingServiceDb.getBookingInfo(1L, 2L));
    }
}