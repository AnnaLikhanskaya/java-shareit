//package item;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.util.LinkedMultiValueMap;
//import ru.practicum.ShareItServer;
//import ru.practicum.booking.dto.BookingInputDto;
//import ru.practicum.booking.dto.BookingStatus;
//import ru.practicum.exception.NotExsistObject;
//import ru.practicum.item.controller.ItemController;
//import ru.practicum.item.dto.CommentDto;
//import ru.practicum.item.dto.ItemDto;
//import ru.practicum.item.mapper.ItemMapper;
//import ru.practicum.item.model.Item;
//import ru.practicum.item.service.CommentService;
//import ru.practicum.item.service.ItemService;
//import ru.practicum.user.dto.UserDto;
//import ru.practicum.user.model.User;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.Matchers.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ItemController.class)
//class ItemControllerTest {
//    @MockBean
//    ItemService itemService;
//
//
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    UserDto userDto;
//    List<ItemDto> itemsDto;
//    ItemDto itemDto;
//
//    @BeforeEach
//    void prepare() {
//        userDto = UserDto.builder()
//                .id(1L)
//                .name("userName")
//                .email("user@mail.ru")
//                .birthday(LocalDate.now().minusYears(20))
//                .build();
//        itemsDto = new ArrayList<>();
//        itemsDto.add(ItemDto.builder()
//                .id(1L)
//                .name("itemName")
//                .description("itemDescription")
//                .available(true)
//                .build());
//        itemsDto.add(ItemDto.builder()
//                .id(2L)
//                .name("anotherItemName")
//                .description("anotherItemDescription")
//                .available(true)
//                .build());
//    }
//
//    @Test
//    void getAllItemsOfUsers() throws Exception {
//        when(itemService.getAllUserItems(anyLong()))
//                .thenReturn(itemsDto);
//        mockMvc.perform(get("/items")
//                        .header("X-Sharer-User-Id", userDto.getId())
//                        .characterEncoding(StandardCharsets.UTF_8))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", is(itemsDto.size()), Integer.class))
//                .andExpect(jsonPath("$[0].id", is(itemsDto.getFirst().getId()), Long.class))
//                .andExpect(jsonPath("$[1].id", is(itemsDto.get(1).getId()), Long.class));
//        verify(itemService, times(1)).getAllUserItems(userDto.getId());
//    }
//
//    @Test
//    void getById() throws Exception {
//        when(itemService.getById(anyLong(), anyLong()))
//                .thenReturn(itemsDto.getFirst());
//        mockMvc.perform(get("/items/{itemId}", itemsDto.getFirst().getId())
//                        .header("X-Sharer-User-Id", userDto.getId())
//                        .characterEncoding(StandardCharsets.UTF_8))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(itemsDto.getFirst().getId()), Long.class))
//                .andExpect(jsonPath("$.name", is(itemsDto.getFirst().getName()), String.class));
//        verify(itemService, times(1)).getById(userDto.getId(), itemsDto.getFirst().getId());
//
//        mockMvc.perform(get("/items/{itemId}", itemsDto.getFirst().getId())
//                        .characterEncoding(StandardCharsets.UTF_8))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    void searchItems() throws Exception {
//        String searchString = "textOfSearch";
//        when(itemService.searchItems(anyLong(), anyString()))
//                .thenReturn(itemsDto);
//        mockMvc.perform(get("/items/search")
//                        .header("X-Sharer-User-Id", userDto.getId())
//                        .param("text", searchString)
//                        .characterEncoding(StandardCharsets.UTF_8))
//                .andExpect(jsonPath("$.length()", is(itemsDto.size())))
//                .andExpect(jsonPath("$[0].id", is(itemsDto.getFirst().getId()), Long.class))
//                .andExpect(jsonPath("$[1].id", is(itemsDto.get(1).getId()), Long.class));
//        verify(itemService, times(1)).searchItems(userDto.getId(), searchString);
//    }
//
//    @Test
//    void createItem() throws Exception {
//        when(itemService.creatItem(any(), anyLong()))
//                .thenReturn(itemsDto.getLast());
//        mockMvc.perform(post("/items")
//                        .header("X-Sharer-User-Id", userDto.getId())
//                        .content(objectMapper.writeValueAsString(itemsDto.getLast()))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", is(itemsDto.getLast().getId()), Long.class));
//        verify(itemService, times(1)).creatItem(itemsDto.getLast(), userDto.getId());
//    }
//
//    @Test
//    void createItemResponse() throws Exception {
//        itemResponseDto = ItemResponseDto.builder()
//                .id(5L)
//                .item(itemsDto.getLast())
//                .responseUser(userDto)
//                .authorName(userDto.getName())
//                .name("nameOfTheResponse")
//                .text("textOfTheResponse")
//                .created(LocalDateTime.now().minusMinutes(10))
//                .build();
//        when(responseService.createResponse(any(), anyLong(), anyLong()))
//                .thenReturn(itemResponseDto);
//        mockMvc.perform(post("/items/{itemId}/comment", itemsDto.getLast().getId())
//                        .header("X-Sharer-User-Id", userDto.getId())
//                        .content(objectMapper.writeValueAsString(itemResponseDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(itemResponseDto.getId()), Long.class))
//                .andExpect(jsonPath("$.item.id", is(itemsDto.getLast().getId()), Long.class));
//        verify(responseService, times(1)).createResponse(itemResponseDto, userDto.getId(), itemsDto.getLast().getId());
//    }
//
//    @Test
//    void updateItem() throws Exception {
//        when(itemService.updateItem(any(), anyLong()))
//                .thenReturn(itemsDto.getLast());
//        mockMvc.perform(patch("/items/{itemId}", itemsDto.getLast().getId())
//                        .header("X-Sharer-User-Id", userDto.getId())
//                        .content(objectMapper.writeValueAsString(itemsDto.getLast()))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", is(itemsDto.getLast().getId()), Long.class));
//        verify(itemService, times(1)).updateItem(itemsDto.getLast(), userDto.getId());
//    }
//
//    @Test
//    void deleteItem() throws Exception {
//        when(itemService.deleteItemById(anyLong(), anyLong()))
//                .thenReturn(itemsDto.getLast());
//        mockMvc.perform(delete("/items/{itemId}", itemsDto.getLast().getId())
//                        .header("X-Sharer-User-Id", userDto.getId())
//                        .characterEncoding(StandardCharsets.UTF_8))
//                .andExpect(jsonPath("$.id", is(itemsDto.getLast().getId()), Long.class))
//                .andExpect(jsonPath("$.name", is(itemsDto.getLast().getName()), String.class));
//        verify(itemService, times(1)).deleteItemById(userDto.getId(), itemsDto.getLast().getId());
//    }
//}