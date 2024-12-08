package item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.item.dto.CommentDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = ShareItGateway.class)
@ExtendWith(MockitoExtension.class)
public class CommentDtoTest {

    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;

    @InjectMocks
    private CommentDto commentDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        id = 1L;
        text = "Comment text";
        authorName = "Author Name";
        created = LocalDateTime.now();

        commentDto = CommentDto.builder()
                .id(id)
                .text(text)
                .authorName(authorName)
                .created(created)
                .build();
    }

    @Test
    public void testCommentDtoBuilder() {
        assertNotNull(commentDto);
        assertEquals(id, commentDto.getId());
        assertEquals(text, commentDto.getText());
        assertEquals(authorName, commentDto.getAuthorName());
        assertEquals(created, commentDto.getCreated());
    }

    @Test
    public void testCommentDtoNoArgsConstructor() {
        CommentDto noArgsCommentDto = new CommentDto();
        assertNotNull(noArgsCommentDto);
    }

    @Test
    public void testCommentDtoAllArgsConstructor() {
        CommentDto allArgsCommentDto = new CommentDto(id, text, authorName, created);
        assertNotNull(allArgsCommentDto);
        assertEquals(id, allArgsCommentDto.getId());
        assertEquals(text, allArgsCommentDto.getText());
        assertEquals(authorName, allArgsCommentDto.getAuthorName());
        assertEquals(created, allArgsCommentDto.getCreated());
    }
}