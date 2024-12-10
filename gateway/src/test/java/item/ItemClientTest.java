package item;


import conf.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ShareItGateway;
import ru.practicum.item.client.ItemClient;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ShareItGateway.class, TestConfig.class})
public class ItemClientTest {

    @MockBean
    private RestTemplate restTemplate;
    private ItemClient itemClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemClient = new ItemClient(restTemplate);
    }

    @Test
    void createItem_ShouldReturnResponseEntity() {
        long userId = 1L;
        ItemDto itemDto = new ItemDto();

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemClient.creatItem(userId, itemDto);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class));
    }

    @Test
    void updateItem_ShouldReturnResponseEntity() {
        long itemId = 1L;
        long userId = 1L;
        ItemDto itemDto = new ItemDto();

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class))
        ).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemClient.updateItem(itemId, userId, itemDto);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class));
    }

    @Test
    void getItemById_ShouldReturnResponseEntity() {
        long itemId = 1L;
        long userId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemClient.getItemById(itemId, userId);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class));
    }

    @Test
    void getItemsByUser_ShouldReturnResponseEntity() {
        long userId = 1L;
        int from = 0;
        int size = 10;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), anyMap())).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemClient.getItemsByUser(userId, from, size);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), anyMap());
    }

    @Test
    void getItemByText_ShouldReturnResponseEntity() {
        String text = "searchText";
        int from = 0;
        int size = 10;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), anyMap())).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemClient.getItemByText(text, from, size);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), anyMap());
    }

    @Test
    void addComment_ShouldReturnResponseEntity() {
        long itemId = 1L;
        long authorId = 1L;
        CommentDto commentDto = new CommentDto();

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new Object());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = itemClient.addComment(itemId, authorId, commentDto);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class));
    }
}