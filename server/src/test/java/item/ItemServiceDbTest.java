package item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServer;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.storage.BookingRepository;
import ru.practicum.exception.InvalidRequestException;
import ru.practicum.exception.NotExsistObject;
import ru.practicum.exception.OtherDataException;
import ru.practicum.item.comment.model.Comment;
import ru.practicum.item.comment.storage.CommentRepository;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.item.paging.CustomPageRequest;
import ru.practicum.item.service.ItemServiceDb;
import ru.practicum.item.storage.ItemRepository;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.storage.ItemRequestRepository;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest

public class ItemServiceDbTest {

    @MockBean
    private ItemRepository itemRepository;

    @Mock
    private CustomPageRequest customPageRequest;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private ItemRequestRepository requestRepository;

    @Autowired
    private ItemServiceDb itemServiceDb;
    private User user;
    private Item item1;
    private Item item2;

    private Item item;
    private ItemDto itemDto;
    private Comment comment;
    private CommentDto commentDto;
    private ItemRequest itemRequest;
    private User author;
    private Booking booking;
    private Booking pastBooking;
    private Booking futureBooking;
    private User owner;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        author = new User();
        author.setId(1L);

        user = new User();
        user.setId(1L);


        item1 = new Item();
        item1.setId(1L);
        item1.setOwner(user);

        item2 = new Item();
        item2.setId(2L);
        item2.setOwner(user);

        item = new Item();
        item.setId(1L);
        item.setName("Item Название");
        item.setDescription("Item Описание");
        item.setAvailable(true);
        item.setOwner(user);

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Item Название")
                .description("Item Описание")
                .available(true)
                .owner(UserMapper.toUserDto(user))
                .comments(List.of())
                .build();

        owner = new User();
        owner.setId(3L);
        item.setOwner(owner);

        comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment.setText("Comment text");
        comment.setItem(item);

        commentDto = CommentDto.builder()
                .id(1L)
                .authorName(user.getName())
                .created(LocalDateTime.now())
                .text("Comment text")
                .build();

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Request Описание");
        itemRequest.setCreated(LocalDateTime.now());

        booking = new Booking();
        booking.setId(1L);
        booking.setBooker(author);
        booking.setItem(item);

        pastBooking = new Booking();
        pastBooking.setId(1L);
        pastBooking.setItem(item);
        pastBooking.setEnd(LocalDateTime.now().minusDays(1));
        pastBooking.setBooker(owner);

        futureBooking = new Booking();
        futureBooking.setId(2L);
        futureBooking.setItem(item);
        futureBooking.setStart(LocalDateTime.now().plusDays(1));
        futureBooking.setBooker(owner);

        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
    }

    @Test
    public void testGetItemsByUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        List<Item> itemPage = Arrays.asList(item1, item2);

        // Настройка моков
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findByOwner(eq(user), any(Pageable.class))).thenReturn(itemPage);

        // Вызов метода
        List<ItemDto> result = itemServiceDb.getItemsByUser(userId, 0, 10);

        // Проверка результатов
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    public void testGetItemById_Owner() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(1L)).thenReturn(Collections.emptyList());
        when(bookingRepository.findByItemAndEndIsBefore(item, LocalDateTime.now())).thenReturn(List.of(pastBooking));
        when(bookingRepository.findByItemAndStartIsAfter(item, LocalDateTime.now())).thenReturn(List.of(futureBooking));

        ItemDto result = itemServiceDb.getItemById(1L, 3L);

        assertNotNull(result, "ItemDto should not be null");
        assertEquals(1L, result.getId(), "Item ID should match");
        assertEquals(Collections.emptyList(), result.getComments(), "Comments should be empty");
        assertEquals(BookingMapper.toDateBookingDto(pastBooking), result.getLastBooking(), "Last booking should match");
        assertEquals(BookingMapper.toDateBookingDto(futureBooking), result.getNextBooking(), "Next booking should match");
        verify(itemRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).findByItemId(1L);
    }

    @Test
    public void testGetItemById_NotOwner() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(1L)).thenReturn(Collections.emptyList());

        ItemDto result = itemServiceDb.getItemById(1L, 2L);

        assertEquals(1L, result.getId());
        assertTrue(result.getComments().isEmpty());
        assertNull(result.getLastBooking());
        assertNull(result.getNextBooking());
        verify(itemRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).findByItemId(1L);
        verify(bookingRepository, never()).findByItemAndEndIsBefore(any(Item.class), any(LocalDateTime.class));
        verify(bookingRepository, never()).findByItemAndStartIsAfter(any(Item.class), any(LocalDateTime.class));
    }

    @Test
    public void testCreatItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item Name");
        itemDto.setDescription("Item Description");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);

        when(itemRepository.save(any(Item.class))).thenReturn(item1);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        ItemDto result = itemServiceDb.creatItem(1L, itemDto);

        assertEquals(1L, result.getId());
    }

    @Test
    public void testUpdateItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.getItemOwner(1L)).thenReturn(user);
        when(commentRepository.findByItemId(1L)).thenReturn(Collections.emptyList());
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDto result = itemServiceDb.updateItem(1L, itemDto, 1L);

        assertEquals("Item Название", result.getName());
        assertEquals("Item Описание", result.getDescription());
        assertEquals(true, result.getAvailable());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testUpdateItem_UserNotOwner() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Updated Name");

        User anotherUser = new User();
        anotherUser.setId(2L);

        when(userRepository.findById(eq(2L))).thenReturn(Optional.of(anotherUser));
        when(itemRepository.findById(eq(1L))).thenReturn(Optional.of(item1));

        assertThrows(OtherDataException.class, () -> itemServiceDb.updateItem(2L, itemDto, 1L));
    }

    @Test
    public void testAddComment_Success() {
        booking.setEnd(LocalDateTime.now().minusDays(1)); // Past booking
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookingRepository.findByBookerAndItem(author, item)).thenReturn(Collections.singletonList(booking));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setId(1L);
            return comment;
        });

        CommentDto result = itemServiceDb.addComment(1L, 1L, commentDto);

        assertEquals("Comment text", result.getText());
        assertNotNull(result.getCreated());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void testAddComment_NotBooker() {
        booking.setEnd(LocalDateTime.now().plusDays(1)); // Future booking
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookingRepository.findByBookerAndItem(author, item)).thenReturn(Collections.emptyList());

        assertThrows(InvalidRequestException.class, () -> itemServiceDb.addComment(1L, 1L, commentDto));
    }

    @Test
    public void testUpdateItemNotOwner() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setName("Вася Васильев");
        anotherUser.setEmail("vasiliy.qqq@epochta.com");

        when(userRepository.findById(eq(2L))).thenReturn(Optional.of(anotherUser));

        assertThrows(NotExsistObject.class, () -> itemServiceDb.updateItem(2L, itemDto, 1L));
    }

    @Test
    public void testAddCommentNotBooker() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setName("Вася Васильев");
        anotherUser.setEmail("vasiliy.qqq@epochta.com");

        when(userRepository.findById(eq(2L))).thenReturn(Optional.of(anotherUser));

        assertThrows(NotExsistObject.class, () -> itemServiceDb.addComment(1L, 2L, commentDto));
    }

    @Test
    public void testGetItemByText_Success() {
        when(itemRepository.search(anyString(), any(Pageable.class))).thenReturn(Collections.singletonList(item));
        when(commentRepository.findByItemId(1L)).thenReturn(Collections.emptyList());

        List<ItemDto> result = itemServiceDb.getItemByText("Test", 0, 10);

        assertEquals(1, result.size());
        assertEquals("Item Название", result.get(0).getName());
        assertEquals("Item Описание", result.get(0).getDescription());
        assertTrue(result.get(0).getAvailable());
        verify(itemRepository, times(1)).search(anyString(), any(Pageable.class));
        verify(commentRepository, times(1)).findByItemId(1L);
    }

    @Test
    public void testGetItemByText_BlankText() {
        List<ItemDto> result = itemServiceDb.getItemByText("", 0, 10);

        assertEquals(Collections.emptyList(), result);
        verify(itemRepository, never()).search(anyString(), any(Pageable.class));
    }

    @Test
    public void testGetItemByText_NoResults() {
        when(itemRepository.search(anyString(), any(Pageable.class))).thenReturn(Collections.emptyList());

        List<ItemDto> result = itemServiceDb.getItemByText("NonExistent", 0, 10);

        assertTrue(result.isEmpty());
        verify(itemRepository, times(1)).search(anyString(), any(Pageable.class));
    }
}