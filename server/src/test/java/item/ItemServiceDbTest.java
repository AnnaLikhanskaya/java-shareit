package item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import ru.practicum.booking.storage.BookingRepository;
import ru.practicum.exception.NotExsistObject;
import ru.practicum.item.comment.model.Comment;
import ru.practicum.item.comment.storage.CommentRepository;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.item.service.ItemServiceDb;
import ru.practicum.item.storage.ItemRepository;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.storage.ItemRequestRepository;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ItemServiceDbTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRequestRepository requestRepository;

    @InjectMocks
    private ItemServiceDb itemServiceDb;

    private User user;
    private Item item;
    private ItemDto itemDto;
    private Comment comment;
    private CommentDto commentDto;
    private ItemRequest itemRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Имя Фамилия");
        user.setEmail("pochta@mail.com");

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
    }

//    @Test
//    public void testGetItemsByUser() {
//        when(itemRepository.findByOwner(any(User.class), any(Pageable.class))).thenReturn(List.of(item));
//
//        List<ItemDto> result = itemServiceDb.getItemsByUser(1L, 0, 10);
//
//        assertEquals(1, result.size());
//        assertEquals(itemDto, result.get(0));
//    }

    @Test
    public void testGetItemByText() {
        when(itemRepository.search(anyString(), any(Pageable.class))).thenReturn(List.of(item));

        List<ItemDto> result = itemServiceDb.getItemByText("item", 0, 10);

        assertEquals(1, result.size());
        assertEquals(itemDto, result.get(0));
    }

    @Test
    public void testGetItemById() {
        when(itemRepository.findById(eq(1L))).thenReturn(Optional.of(item));

        ItemDto result = itemServiceDb.getItemById(1L, 1L);

        assertEquals(itemDto, result);
    }

//    @Test
//    public void testCreateItem() {
//        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
//        when(itemRepository.save(any(Item.class))).thenReturn(item);
//
//        ItemDto result = itemServiceDb.creatItem(1L, itemDto);
//
//        assertEquals(itemDto, result);
//    }
//
//    @Test
//    public void testUpdateItem() {
//        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
//        when(itemRepository.findById(eq(1L))).thenReturn(Optional.of(item));
//        when(itemRepository.save(any(Item.class))).thenReturn(item);
//
//        ItemDto result = itemServiceDb.updateItem(1L, itemDto, 1L);
//
//        assertEquals(itemDto, result);
//    }
//
//    @Test
//    public void testAddComment() {
//        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
//        when(itemRepository.findById(eq(1L))).thenReturn(Optional.of(item));
//        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
//
//        CommentDto result = itemServiceDb.addComment(1L, 1L, commentDto);
//
//        assertEquals(commentDto, result);
//    }

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
}