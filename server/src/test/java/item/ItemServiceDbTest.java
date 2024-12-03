package item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServer;
import ru.practicum.exception.NotExsistObject;
import ru.practicum.item.comment.model.Comment;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.item.service.ItemServiceDb;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest
public class ItemServiceDbTest {

    @Mock
    private UserRepository userRepository;

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