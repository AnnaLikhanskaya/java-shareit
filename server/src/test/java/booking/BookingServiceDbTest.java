package booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServer;
import ru.practicum.booking.dto.BookingInputDto;
import ru.practicum.booking.dto.BookingOutputDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.booking.dto.BookingStatus;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.service.BookingServiceDb;
import ru.practicum.booking.storage.BookingRepository;
import ru.practicum.exception.InvalidRequestException;
import ru.practicum.exception.OtherDataException;
import ru.practicum.exception.UnknownStateException;
import ru.practicum.item.model.Item;
import ru.practicum.item.paging.CustomPageRequest;
import ru.practicum.item.storage.ItemRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.booking.dto.BookingState.INCORRECT;

@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest
public class BookingServiceDbTest {

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private BookingServiceDb bookingServiceDb;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Тесты для метода createBooking
    @Test
    public void testCreateBooking() {
        BookingInputDto bookingInputDto = new BookingInputDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setAvailable(true);
        item.setOwner(new User());
        item.getOwner().setId(2L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingInputDto.getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingOutputDto result = bookingServiceDb.createBooking(bookingInputDto, userId);

        assertNotNull(result);
        assertEquals(BookingStatus.WAITING, result.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void testCreateBooking_ItemNotAvailable() {
        BookingInputDto bookingInputDto = new BookingInputDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setAvailable(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingInputDto.getItemId())).thenReturn(Optional.of(item));

        assertThrows(InvalidRequestException.class, () -> bookingServiceDb.createBooking(bookingInputDto, userId));
    }

    @Test
    public void testCreateBooking_UserIsOwner() {
        BookingInputDto bookingInputDto = new BookingInputDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setAvailable(true);
        item.setOwner(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingInputDto.getItemId())).thenReturn(Optional.of(item));

        assertThrows(OtherDataException.class, () -> bookingServiceDb.createBooking(bookingInputDto, userId));
    }

    // Тесты для метода updateApprove
    @Test
    public void testUpdateApprove() {
        Long bookingId = 1L;
        Boolean approved = true;
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);
        Item item = new Item();
        item.setOwner(user);
        booking.setItem(item);
        booking.setBooker(User.builder().id(2L).build());

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(User.builder().id(2L).build()));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingOutputDto result = bookingServiceDb.updateApprove(bookingId, approved, userId);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void testUpdateApprove_NotOwner() {
        Long bookingId = 1L;
        Boolean approved = true;
        Long userId = 2L;
        User user = new User();
        user.setId(userId);
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);
        Item item = new Item();
        item.setOwner(new User());
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(OtherDataException.class, () -> bookingServiceDb.updateApprove(bookingId, approved, userId));
    }

    @Test
    public void testUpdateApprove_NotWaiting() {
        Long bookingId = 1L;
        Boolean approved = true;
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.APPROVED);
        Item item = new Item();
        item.setOwner(user);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(InvalidRequestException.class, () -> bookingServiceDb.updateApprove(bookingId, approved, userId));
    }

    // Тесты для метода getBookingInfo
    @Test
    public void testGetBookingInfo() {
        Long bookingId = 1L;
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Booking booking = new Booking();
        Item item = new Item();
        item.setOwner(user);
        booking.setItem(item);
        booking.setBooker(user);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        BookingOutputDto result = bookingServiceDb.getBookingInfo(bookingId, userId);

        assertNotNull(result);
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    public void testGetBookingInfo_NotOwnerOrBooker() {
        Long bookingId = 1L;
        Long userId = 2L;
        User user = new User();
        user.setId(userId);
        Booking booking = new Booking();
        Item item = new Item();
        item.setOwner(new User());
        booking.setItem(item);
        booking.setBooker(new User());

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(OtherDataException.class, () -> bookingServiceDb.getBookingInfo(bookingId, userId));
    }

    // Тесты для метода getAllBookings
    @Test
    public void testGetAllBookings_ALL() {
        Long bookerId = 1L;
        BookingState state = BookingState.ALL;
        Integer from = 0;
        Integer size = 10;
        User booker = new User();
        booker.setId(bookerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBooker(booker, pageable)).thenReturn(Collections.emptyList());

        List<BookingOutputDto> result = bookingServiceDb.getAllBookings(bookerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByBooker(booker, pageable);
    }

    @Test
    public void testGetAllBookings_CURRENT() {
        Long bookerId = 1L;
        BookingState state = BookingState.CURRENT;
        Integer from = 0;
        Integer size = 10;
        User booker = new User();
        booker.setId(bookerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.findCurrentByBooker(booker, pageable)).thenReturn(Collections.emptyList());

        List<BookingOutputDto> result = bookingServiceDb.getAllBookings(bookerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findCurrentByBooker(booker, pageable);
    }

    @Test
    public void testGetAllBookings_PAST() {
        Long bookerId = 1L;
        BookingState state = BookingState.PAST;
        Integer from = 0;
        Integer size = 10;
        User booker = new User();
        booker.setId(bookerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerAndEndIsBefore(booker, LocalDateTime.now(), pageable)).thenReturn(Collections.emptyList());

        List<BookingOutputDto> result = bookingServiceDb.getAllBookings(bookerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByBookerAndEndIsBefore(booker, LocalDateTime.now(), pageable);
    }

    @Test
    public void testGetAllBookings_FUTURE() {
        Long bookerId = 1L;
        BookingState state = BookingState.FUTURE;
        Integer from = 0;
        Integer size = 10;
        User booker = new User();
        booker.setId(bookerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerAndStartIsAfter(booker, LocalDateTime.now(), pageable)).thenReturn(Collections.emptyList());

        List<BookingOutputDto> result = bookingServiceDb.getAllBookings(bookerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByBookerAndStartIsAfter(booker, LocalDateTime.now(), pageable);
    }

    @Test
    public void testGetAllBookings_WAITING() {
        Long bookerId = 1L;
        BookingState state = BookingState.WAITING;
        Integer from = 0;
        Integer size = 10;
        User booker = new User();
        booker.setId(bookerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerAndStatus(booker, BookingStatus.WAITING, pageable)).thenReturn(Collections.emptyList());

        List<BookingOutputDto> result = bookingServiceDb.getAllBookings(bookerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByBookerAndStatus(booker, BookingStatus.WAITING, pageable);
    }

    @Test
    public void testGetAllBookings_REJECTED() {
        Long bookerId = 1L;
        BookingState state = BookingState.REJECTED;
        Integer from = 0;
        Integer size = 10;
        User booker = new User();
        booker.setId(bookerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerAndStatus(booker, BookingStatus.REJECTED, pageable)).thenReturn(Collections.emptyList());

        List<BookingOutputDto> result = bookingServiceDb.getAllBookings(bookerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByBookerAndStatus(booker, BookingStatus.REJECTED, pageable);
    }

    @Test
    public void testGetAllBookings_UnknownState() {
        Long bookerId = 1L;
        BookingState state = null;
        Integer from = 0;
        Integer size = 10;
        User booker = new User();
        booker.setId(bookerId);

        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));

        assertThrows(NullPointerException.class, () -> bookingServiceDb.getAllBookings(bookerId, state, from, size));
    }

    // Тесты для метода getAllBookingsForOwner
    @Test
    public void testGetAllBookingsForOwner_ALL() {
        Long ownerId = 1L;
        BookingState state = BookingState.ALL;
        Integer from = 0;
        Integer size = 10;
        User owner = new User();
        owner.setId(ownerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findByOwner(owner, pageable)).thenReturn(Collections.emptyList());

        List<BookingOutputDto> result = bookingServiceDb.getAllBookingsForOwner(ownerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByOwner(owner, pageable);
    }

    @Test
    public void testGetAllBookingsForOwner_CURRENT() {
        Long ownerId = 1L;
        BookingState state = BookingState.CURRENT;
        Integer from = 0;
        Integer size = 10;
        User owner = new User();
        owner.setId(ownerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findCurrentByOwner(owner, pageable)).thenReturn(Collections.emptyList());

        List<BookingOutputDto> result = bookingServiceDb.getAllBookingsForOwner(ownerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findCurrentByOwner(owner, pageable);
    }

    @Test
    public void testGetAllBookingsForOwner_PAST() {
        Long ownerId = 1L;
        BookingState state = BookingState.PAST;
        Integer from = 0;
        Integer size = 10;
        User owner = new User();
        owner.setId(ownerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findByOwnerAndEndIsBefore(any(), any(), any())).thenReturn(Collections.emptyList());

        List<BookingOutputDto> result = bookingServiceDb.getAllBookingsForOwner(ownerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByOwnerAndEndIsBefore(any(), any(), any());
    }

    @Test
    public void testGetAllBookings_Incorrect() {
        Long ownerId = 1L;
        Integer from = 0;
        Integer size = 10;
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(User.builder().id(1L).build()));

        assertThrows(UnknownStateException.class,
                () -> bookingServiceDb.getAllBookings(ownerId, INCORRECT, from, size));
    }

    @Test
    public void testGetAllBookingsForOwner_Incorrect() {
        Long ownerId = 1L;
        Integer from = 0;
        Integer size = 10;
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(User.builder().id(1L).build()));

        assertThrows(UnknownStateException.class,
                () -> bookingServiceDb.getAllBookingsForOwner(ownerId, INCORRECT, from, size));
    }

    @Test
    public void testGetAllBookingsForOwner_FUTURE() {
        Long ownerId = 1L;
        BookingState state = BookingState.FUTURE;
        Integer from = 0;
        Integer size = 10;
        User owner = new User();
        owner.setId(ownerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        // Фиксированное время
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findByOwnerAndStartIsAfter(any(), any(), any())).thenReturn(List.of());

        // Используем фиксированное время в методе
        List<BookingOutputDto> result = bookingServiceDb.getAllBookingsForOwner(ownerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByOwnerAndStartIsAfter(any(), any(), any());
    }

    @Test
    public void testGetAllBookingsForOwner_WAITING() {
        Long ownerId = 1L;
        BookingState state = BookingState.WAITING;
        Integer from = 0;
        Integer size = 10;
        User owner = new User();
        owner.setId(ownerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findByOwnerAndStatus(owner, BookingStatus.WAITING, pageable)).thenReturn(Collections.emptyList());

        List<BookingOutputDto> result = bookingServiceDb.getAllBookingsForOwner(ownerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByOwnerAndStatus(owner, BookingStatus.WAITING, pageable);
    }

    @Test
    public void testGetAllBookingsForOwner_REJECTED() {
        Long ownerId = 1L;
        BookingState state = BookingState.REJECTED;
        Integer from = 0;
        Integer size = 10;
        User owner = new User();
        owner.setId(ownerId);
        Pageable pageable = CustomPageRequest.create(from, size, Sort.by(Sort.Direction.DESC, "start"));

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findByOwnerAndStatus(owner, BookingStatus.REJECTED, pageable)).thenReturn(Collections.emptyList());

        List<BookingOutputDto> result = bookingServiceDb.getAllBookingsForOwner(ownerId, state, from, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByOwnerAndStatus(owner, BookingStatus.REJECTED, pageable);
    }

    @Test
    public void testGetAllBookingsForOwner_UnknownState() {
        Long ownerId = 1L;
        BookingState state = null;
        Integer from = 0;
        Integer size = 10;
        User owner = new User();
        owner.setId(ownerId);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        assertThrows(NullPointerException.class, () -> bookingServiceDb.getAllBookingsForOwner(ownerId, state, from, size));
    }

    // Тесты для метода isTheTimeCorrect
    @Test
    public void testIsTheTimeCorrect_Valid() {
        BookingInputDto bookingInputDto = new BookingInputDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        assertDoesNotThrow(() -> bookingServiceDb.isTheTimeCorrect(bookingInputDto));
    }

    @Test
    public void testIsTheTimeCorrect_Invalid() {
        BookingInputDto bookingInputDto = new BookingInputDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now());

        assertThrows(InvalidRequestException.class, () -> bookingServiceDb.isTheTimeCorrect(bookingInputDto));
    }
}