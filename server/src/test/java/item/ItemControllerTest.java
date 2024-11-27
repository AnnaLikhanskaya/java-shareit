//package item;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.util.LinkedMultiValueMap;
//import ru.practicum.ShareItServer;
//import ru.practicum.exception.NotExsistObject;
//import ru.practicum.item.controller.ItemController;
//import ru.practicum.item.dto.CommentDto;
//import ru.practicum.item.dto.ItemDto;
//import ru.practicum.item.mapper.ItemMapper;
//import ru.practicum.item.model.Item;
//import ru.practicum.item.service.CommentService;
//import ru.practicum.item.service.ItemService;
//import ru.practicum.user.model.User;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ItemController.class)
//@SpringBootTest(classes = ShareItServer.class)
//public class ItemControllerTest {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ItemService itemService;
//
//    @MockBean
//    private CommentService commentService;
//
//    private User user;
//    private User incorrectOwner;
//    private Item itemCorrect;
//
//    @BeforeEach
//    void beforeEach() {
//        user = new User(1L, "correct", "forItem@mail.ru");
//        incorrectOwner = new User(4L, "incorrect", "incorrect@mail.ru");
//        itemCorrect = new Item(1L, "correct", "correct desc", true, user, null);
//    }
//
//    @SneakyThrows
//    @Test
//    void itemCreate_whenItemCorrect_thenReturnedOk() {
//        ItemDto itemDto = ItemMapper.toItemDto(itemCorrect);
//        when(itemService.creatItem(1L, itemDto)).thenReturn(itemDto);
//
//        mockMvc.perform(post("/items")
//                        .content(objectMapper.writeValueAsString(itemDto))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Sharer-User-Id", 1))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
//
//        verify(itemService, times(1)).creatItem(anyLong(), any());
//    }
//
//    @SneakyThrows
//    @Test
//    void itemCreate_whenUserIdNotPresent_thenReturnedClientError() {
//        ItemDto itemDto = ItemMapper.toItemDto(itemCorrect);
//
//        mockMvc.perform(post("/items")
//                        .content(objectMapper.writeValueAsString(itemDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"error\":\"Required request header 'X-Sharer-User-Id' for method parameter type Long is not present\"}"));
//
//        verify(itemService, never()).creatItem(anyLong(), any());
//    }
//
//    @SneakyThrows
//    @Test
//    void itemCreate_whenIncorrectOwner_thenReturnedClientError() {
//        ItemDto itemDto = ItemMapper.toItemDto(new Item(1L, "correct", "correct desc", true, incorrectOwner, null));
//        when(itemService.creatItem(2L, itemDto)).thenThrow(new NotExsistObject("Невозможно найти. Пользователь отсутствует!"));
//
//        mockMvc.perform(post("/items")
//                        .content(objectMapper.writeValueAsString(itemDto))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Sharer-User-Id", 2))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"error\":\"Невозможно найти. Пользователь отсутствует!\"}"));
//
//        verify(itemService, times(1)).creatItem(anyLong(), any());
//    }
//
//    @SneakyThrows
//    @Test
//    void updateItem_whenItemCorrect_thenReturnedOk() {
//        ItemDto itemDto = ItemMapper.toItemDto(new Item(1L, "update", "all update", false, user, null));
//        when(itemService.updateItem(1L, itemDto, 1L)).thenReturn(itemDto);
//
//        mockMvc.perform(patch("/items/{itemId}", 1L)
//                        .content(objectMapper.writeValueAsString(itemDto))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Sharer-User-Id", 1))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
//
//        verify(itemService, times(1)).updateItem(anyLong(), any(), anyLong());
//    }
//
//    @SneakyThrows
//    @Test
//    void updateItem_whenIncorrectOwner_thenReturnedClientError() {
//        ItemDto itemDto = ItemMapper.toItemDto(new Item(1L, "correct", "correct desc", true, incorrectOwner, null));
//        when(itemService.updateItem(2L, itemDto, 1L)).thenThrow(new NotExsistObject("Невозможно найти. Пользователь отсутствует!"));
//
//        mockMvc.perform(patch("/items/{itemId}", 1)
//                        .content(objectMapper.writeValueAsString(itemDto))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Sharer-User-Id", 2))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"error\":\"Невозможно найти. Пользователь отсутствует!\"}"));
//
//        verify(itemService, times(1)).updateItem(anyLong(), any(), anyLong());
//    }
//
//    @SneakyThrows
//    @Test
//    void updateItem_whenUserIdNotPresent_thenReturnedClientError() {
//        ItemDto itemDto = ItemMapper.toItemDto(itemCorrect);
//
//        mockMvc.perform(patch("/items/{itemId}", 1)
//                        .content(objectMapper.writeValueAsString(itemDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"error\":\"Required request header 'X-Sharer-User-Id' for method parameter type Long is not present\"}"));
//
//        verify(itemService, never()).updateItem(anyLong(), any(), anyLong());
//    }
//
//    @SneakyThrows
//    @Test
//    void getItemsByUser_whenCorrectOwner_thenReturnedOk() {
//        ItemDto itemDto = ItemMapper.toItemDto(itemCorrect);
//        List<ItemDto> items = List.of(itemDto);
//        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
//        requestParams.add("from", "0");
//        requestParams.add("size", "10");
//        when(itemService.getItemsByUser(1L, 0, 10)).thenReturn(items);
//
//        mockMvc.perform(get("/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .params(requestParams)
//                        .header("X-Sharer-User-Id", 1))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(items)));
//
//        verify(itemService, times(1)).getItemsByUser(anyLong(), anyInt(), anyInt());
//    }
//
//    @SneakyThrows
//    @Test
//    void getItemsByUser_whenNotOwner_thenReturnedClientError() {
//        ItemDto itemDto = ItemMapper.toItemDto(itemCorrect);
//        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
//        requestParams.add("from", "0");
//        requestParams.add("size", "10");
//
//        mockMvc.perform(get("/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .params(requestParams))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"error\":\"Required request header 'X-Sharer-User-Id' for method parameter type Long is not present\"}"));
//
//        verify(itemService, never()).getItemsByUser(anyLong(), anyInt(), anyInt());
//    }
//
//    @SneakyThrows
//    @Test
//    void getItemsByText_whenSearch_thenReturnedOk() {
//        ItemDto itemDto = ItemMapper.toItemDto(itemCorrect);
//        List<ItemDto> items = List.of(itemDto);
//        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
//        requestParams.add("text", "test");
//        requestParams.add("from", "0");
//        requestParams.add("size", "10");
//        when(itemService.getItemByText("test", 0, 10)).thenReturn(items);
//
//        mockMvc.perform(get("/items/search")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .params(requestParams)
//                        .header("X-Sharer-User-Id", 1))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(items)));
//
//        verify(itemService, times(1)).getItemByText(anyString(), anyInt(), anyInt());
//    }
//
//    @SneakyThrows
//    @Test
//    void getItemById_whenOwnerPresent_thenReturnedOk() {
//        ItemDto itemDto = ItemMapper.toItemDto(itemCorrect);
//        when(itemService.getItemById(1L, 1L)).thenReturn(itemDto);
//
//        mockMvc.perform(get("/items/{userId}", 1)
//                        .header("X-Sharer-User-Id", 1))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
//
//        verify(itemService).getItemById(anyLong(), anyLong());
//    }
//
//    @SneakyThrows
//    @Test
//    void getItemById_whenOwnerNotPresent_thenReturnedClientError() {
//        mockMvc.perform(get("/items/{userId}", 1))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"error\":\"Required request header 'X-Sharer-User-Id' for method parameter type Long is not present\"}"));
//
//        verify(itemService, never()).getItemById(anyLong(), anyLong());
//    }
//
//    @SneakyThrows
//    @Test
//    void getItemById_whenItemIdNotFound_thenReturnedClientError() {
//        when(itemService.getItemById(2L, 1L)).thenThrow(new NotExsistObject("Невозможно найти. вещь отсутствует!"));
//
//        mockMvc.perform(get("/items/{userId}", 2)
//                        .header("X-Sharer-User-Id", 1))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"error\":\"Невозможно найти. вещь отсутствует!\"}"));
//
//        verify(itemService).getItemById(anyLong(), anyLong());
//    }
//
//    @SneakyThrows
//    @Test
//    void postComment_whenCorrectComment_thenReturnedOk() {
//        CommentDto comment = CommentDto.builder().text("test").build();
//        when(commentService.addComment(1L, 1L, comment)).thenReturn(comment);
//
//        mockMvc.perform(post("/items/{itemId}/comment", 1)
//                        .content(objectMapper.writeValueAsString(comment))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Sharer-User-Id", 1))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(comment)));
//
//        verify(commentService).addComment(anyLong(), anyLong(), any());
//    }
//
//    @SneakyThrows
//    @Test
//    void postComment_whenCorrectNotUserId_thenReturnedClientError() {
//        CommentDto comment = CommentDto.builder().text("test").build();
//
//        mockMvc.perform(post("/items/{itemId}/comment", 1)
//                        .content(objectMapper.writeValueAsString(comment))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"error\":\"Required request header 'X-Sharer-User-Id' for method parameter type Long is not present\"}"));
//
//        verify(commentService, never()).addComment(anyLong(), anyLong(), any());
//    }
//}