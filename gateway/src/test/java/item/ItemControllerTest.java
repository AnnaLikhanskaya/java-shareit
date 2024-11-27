package item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.item.client.ItemClient;
import ru.practicum.item.controller.ItemController;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    @Mock
    private ItemClient itemClient;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetItemsByUser() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(itemClient.getItemsByUser(eq(userId), eq(from), eq(size))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.getItemsByUser(userId, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetItemByText() {
        String text = "item";
        Integer from = 0;
        Integer size = 10;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(itemClient.getItemByText(eq(text), eq(from), eq(size))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.getItemByText(text, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetItemById() {
        Long userId = 1L;
        Long itemId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(itemClient.getItemById(eq(itemId), eq(userId))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.getItemById(userId, itemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateItem() {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(itemClient.creatItem(eq(userId), eq(itemDto))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.creatItem(userId, itemDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateItem() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(itemClient.updateItem(eq(itemId), eq(userId), eq(itemDto))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.updateItem(userId, itemDto, itemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testPostComment() {
        Long itemId = 1L;
        Long authorId = 1L;
        CommentDto commentDto = new CommentDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(itemClient.addComment(eq(itemId), eq(authorId), eq(commentDto))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.postComment(itemId, authorId, commentDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}