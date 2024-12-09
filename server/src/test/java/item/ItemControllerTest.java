package item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.item.controller.ItemController;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.service.CommentService;
import ru.practicum.item.service.ItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetItemsByUser() throws Exception {
        Long userId = 1L;
        List<ItemDto> items = List.of(ItemDto.builder().id(1L).name("Item 1").build());

        when(itemService.getItemsByUser(eq(userId), any(Integer.class), any(Integer.class))).thenReturn(items);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(items)));
    }

    @Test
    public void testGetItemByText() throws Exception {
        String text = "searchText";
        List<ItemDto> items = List.of(ItemDto.builder().id(1L).name("Item 1").build());

        when(itemService.getItemByText(eq(text), any(Integer.class), any(Integer.class))).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(items)));
    }

    @Test
    public void testGetItemById() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto item = ItemDto.builder().id(itemId).name("Item 1").build();

        when(itemService.getItemById(eq(itemId), eq(userId))).thenReturn(item);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(item)));
    }

    @Test
    public void testCreateItem() throws Exception {
        Long userId = 1L;
        ItemDto itemDto = ItemDto.builder().id(1L).name("Item 1").build();

        when(itemService.creatItem(eq(userId), eq(itemDto))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    public void testUpdateItem() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto itemDto = ItemDto.builder().id(itemId).name("Item 1").build();

        when(itemService.updateItem(eq(userId), eq(itemDto), eq(itemId))).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    public void testPostComment() throws Exception {
        Long itemId = 1L;
        Long authorId = 1L;
        CommentDto commentDto = CommentDto.builder().id(1L).text("Comment text").build();

        when(commentService.addComment(eq(itemId), eq(authorId), eq(commentDto))).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentDto)));
    }

}