package booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import ru.practicum.booking.dto.BookingInputDto;
import ru.practicum.booking.dto.BookingOutputDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.booking.dto.BookingStatus;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.service.BookingServiceDb;
import ru.practicum.booking.storage.BookingRepository;
import ru.practicum.exception.InvalidRequestException;
import ru.practicum.exception.OtherDataException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.model.Item;
import ru.practicum.item.storage.ItemRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class BookingServiceDbTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceDb bookingServiceDb;

    private User user;
    private Item item;
    private BookingInputDto bookingInputDto;
    private Booking booking;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Костя Константинов");
        user.setEmail("konstantin.123@pochta.com");

        item = new Item();
        item.setId(1L);
        item.setName("Item Название");
        item.setDescription("Item Описание");
        item.setAvailable(true);
        item.setOwner(user);

        bookingInputDto = new BookingInputDto();
        bookingInputDto.setItemId(1L);
        bookingInputDto.setStart(LocalDateTime.now().plusHours(1));
        bookingInputDto.setEnd(LocalDateTime.now().plusHours(2));

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(bookingInputDto.getStart());
        booking.setEnd(bookingInputDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
    }

    @Test
    public void testCreateBookingItemNotAvailable() {
        item.setAvailable(false);
        when(itemRepository.findById(eq(1L))).thenReturn(Optional.of(item));

        assertThrows(UserNotFoundException.class, () -> bookingServiceDb.createBooking(bookingInputDto, 1L));
    }

    @Test
    public void testCreateBookingUserIsOwner() {
        when(itemRepository.findById(eq(1L))).thenReturn(Optional.of(item));

        assertThrows(UserNotFoundException.class, () -> bookingServiceDb.createBooking(bookingInputDto, 1L));
    }

    @Test
    public void testUpdateApprove() {
        when(bookingRepository.findById(eq(1L))).thenReturn(Optional.of(booking));
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingOutputDto result = bookingServiceDb.updateApprove(1L, true, 1L);

        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    public void testUpdateApproveNotOwner() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        user.setName("Костя Константинов");
        user.setEmail("konstantin.123@pochta.com");

        when(bookingRepository.findById(eq(1L))).thenReturn(Optional.of(booking));
        when(userRepository.findById(eq(2L))).thenReturn(Optional.of(anotherUser));

        assertThrows(OtherDataException.class, () -> bookingServiceDb.updateApprove(1L, true, 2L));
    }

    @Test
    public void testUpdateApproveNotWaiting() {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(eq(1L))).thenReturn(Optional.of(booking));
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));

        assertThrows(InvalidRequestException.class, () -> bookingServiceDb.updateApprove(1L, true, 1L));
    }

    @Test
    public void testGetBookingInfo() {
        when(bookingRepository.findById(eq(1L))).thenReturn(Optional.of(booking));
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));

        BookingOutputDto result = bookingServiceDb.getBookingInfo(1L, 1L);

        assertEquals(1L, result.getId());
        assertEquals(bookingInputDto.getStart(), result.getStart());
        assertEquals(bookingInputDto.getEnd(), result.getEnd());
        assertEquals(BookingStatus.WAITING, result.getStatus());
    }

    @Test
    public void testGetBookingInfoNotOwnerOrBooker() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        user.setName("Костя Константинов");
        user.setEmail("konstantin.123@pochta.com");

        when(bookingRepository.findById(eq(1L))).thenReturn(Optional.of(booking));
        when(userRepository.findById(eq(2L))).thenReturn(Optional.of(anotherUser));

        assertThrows(OtherDataException.class, () -> bookingServiceDb.getBookingInfo(1L, 2L));
    }

    @Test
    public void testGetAllBookings() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findByBooker(any(User.class), any(Pageable.class))).thenReturn(List.of(booking));

        List<BookingOutputDto> result = bookingServiceDb.getAllBookings(1L, BookingState.ALL, 0, 10);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(bookingInputDto.getStart(), result.get(0).getStart());
        assertEquals(bookingInputDto.getEnd(), result.get(0).getEnd());
        assertEquals(BookingStatus.WAITING, result.get(0).getStatus());
    }

    @Test
    public void testGetAllBookingsForOwner() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findByOwner(any(User.class), any(Pageable.class))).thenReturn(List.of(booking));

        List<BookingOutputDto> result = bookingServiceDb.getAllBookingsForOwner(1L, BookingState.ALL, 0, 10);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(bookingInputDto.getStart(), result.get(0).getStart());
        assertEquals(bookingInputDto.getEnd(), result.get(0).getEnd());
        assertEquals(BookingStatus.WAITING, result.get(0).getStatus());
    }
}
