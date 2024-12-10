package item.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServer;
import ru.practicum.item.comment.model.Comment;
import ru.practicum.item.comment.storage.CommentRepository;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest
public class CommentRepositoryTest {

    @Mock
    private CommentRepository commentRepository;
    private Comment comment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(new User());
        comment.setCreated(java.time.LocalDateTime.now());
        comment.setText("Comment text");
        comment.setItem(new Item());
    }

    @Test
    public void testFindByItemId() {
        when(commentRepository.findByItemId(any(Long.class))).thenReturn(List.of(comment));

        List<Comment> result = commentRepository.findByItemId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(comment, result.get(0));
    }
}