package item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.item.client.ItemClient;
import ru.practicum.item.controller.ItemController;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = ShareItGateway.class)
@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemClient itemClient;

    @InjectMocks
    private ItemController itemController;

    private Long userId;
    private Long itemId;
    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        userId = 1L;
        itemId = 1L;
        itemDto = new ItemDto();
        commentDto = new CommentDto();
    }

    @Test
    void getItemsByUser_shouldReturnItems() {
        when(itemClient.getItemsByUser(userId, null, null)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemController.getItemsByUser(userId, null, null);

        verify(itemClient, times(1)).getItemsByUser(userId, null, null);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void getItemByText_shouldReturnItems() {
        String text = "item";
        when(itemClient.getItemByText(text, null, null)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemController.getItemByText(text, null, null);

        verify(itemClient, times(1)).getItemByText(text, null, null);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void getItemById_shouldReturnItem() {
        when(itemClient.getItemById(itemId, userId)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemController.getItemById(userId, itemId);

        verify(itemClient, times(1)).getItemById(itemId, userId);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void creatItem_shouldCreateItem() {
        when(itemClient.creatItem(userId, itemDto)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemController.creatItem(userId, itemDto);

        verify(itemClient, times(1)).creatItem(userId, itemDto);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void updateItem_shouldUpdateItem() {
        when(itemClient.updateItem(itemId, userId, itemDto)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemController.updateItem(userId, itemDto, itemId);

        verify(itemClient, times(1)).updateItem(itemId, userId, itemDto);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void postComment_shouldAddComment() {
        when(itemClient.addComment(itemId, userId, commentDto)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemController.postComment(itemId, userId, commentDto);

        verify(itemClient, times(1)).addComment(itemId, userId, commentDto);
        assertEquals(ResponseEntity.ok().build(), response);
    }
}