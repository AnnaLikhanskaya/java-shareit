package itemrequest;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.item.dto.ItemForRequestDto;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.mapper.ItemRequestMapper;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ContextConfiguration(classes = Short.class)

public class ItemRequestMapperTest {

    @Test
    public void testToItemRequestDtoWithAnswers() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Test Description")
                .created(LocalDateTime.now())
                .build();

        List<ItemForRequestDto> answers = Arrays.asList(
                new ItemForRequestDto(1L, "Item 1", "Description 1", true, 1L),
                new ItemForRequestDto(2L, "Item 2", "Description 2", false, 2L)
        );

        ItemRequestDto result = ItemRequestMapper.toItemRequestDto(itemRequest, answers);

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemRequest.getCreated(), result.getCreated());
        assertEquals(answers, result.getItems());
    }

    @Test
    public void testToItemRequestDtoWithoutAnswers() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Test Description")
                .created(LocalDateTime.now())
                .build();

        ItemRequestDto result = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemRequest.getCreated(), result.getCreated());
    }

    @Test
    public void testToItemRequest() {
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Test Description")
                .created(LocalDateTime.now())
                .build();

        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        ItemRequest result = ItemRequestMapper.toItemRequest(requestDto, user);

        assertNotNull(result);
        assertEquals(requestDto.getId(), result.getId());
        assertEquals(requestDto.getDescription(), result.getDescription());
        assertEquals(requestDto.getCreated(), result.getCreated());
        assertEquals(user, result.getRequestor());
    }
}