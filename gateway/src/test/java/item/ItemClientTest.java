//package item;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//import ru.practicum.ShareItGateway;
//import ru.practicum.item.client.ItemClient;
//import ru.practicum.item.dto.CommentDto;
//import ru.practicum.item.dto.ItemDto;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = ShareItGateway.class)
//@ExtendWith(MockitoExtension.class)
//public class ItemClientTest {
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @InjectMocks
//    private ItemClient itemClient;
//
//    @Test
//    void testCreateItem() {
//        ItemDto itemDto = new ItemDto();
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = itemClient.creatItem(1L, itemDto);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testUpdateItem() {
//        ItemDto itemDto = new ItemDto();
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = itemClient.updateItem(1L, 1L, itemDto);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testGetItemById() {
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = itemClient.getItemById(1L, 1L);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testGetItemsByUser() {
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("from", 0);
//        parameters.put("size", 10);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), eq(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = itemClient.getItemsByUser(1L, 0, 10);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testGetItemByText() {
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("text", "test");
//        parameters.put("from", 0);
//        parameters.put("size", 10);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), eq(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = itemClient.getItemByText("test", 0, 10);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//
//    @Test
//    void testAddComment() {
//        CommentDto commentDto = new CommentDto();
//        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class), any(Map.class)))
//                .thenReturn(responseEntity);
//
//        ResponseEntity<Object> result = itemClient.addComment(1L, 1L, commentDto);
//
//        assertThat(result).isEqualTo(responseEntity);
//    }
//}