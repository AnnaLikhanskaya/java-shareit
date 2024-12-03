package item.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServer;
import ru.practicum.item.comment.mapper.CommentMapper;
import ru.practicum.item.comment.model.Comment;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest

public class CommentMapperTest {

    @Mock
    private User author;

    @Mock
    private Item item;

    @InjectMocks
    private CommentMapper commentMapper;

    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        author = new User();
        author.setId(1L);
        author.setName("John Doe");
        author.setEmail("john.doe@example.com");

        item = new Item();
        item.setId(1L);
        item.setName("Item Name");
        item.setDescription("Item Description");
        item.setAvailable(true);
        item.setOwner(author);

        comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        comment.setText("Comment text");
        comment.setItem(item);

        commentDto = CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    @Test
    public void testToCommentDto() {
        CommentDto result = CommentMapper.toCommentDto(comment);

        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getAuthor().getName(), result.getAuthorName());
        assertEquals(comment.getCreated(), result.getCreated());
    }

    @Test
    public void testToComment() {
        Comment result = CommentMapper.toComment(commentDto, author, item);

        assertNotNull(result);
        assertEquals(commentDto.getId(), result.getId());
        assertEquals(commentDto.getText(), result.getText());
        assertEquals(commentDto.getAuthorName(), result.getAuthor().getName());
        assertEquals(commentDto.getCreated(), result.getCreated());
        assertEquals(item, result.getItem());
    }
}