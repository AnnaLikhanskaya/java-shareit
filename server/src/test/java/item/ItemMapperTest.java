package item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.booking.dto.DateBookingDto;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemForRequestDto;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemMapperTest {

    @Mock
    private Item item;

    @Mock
    private List<CommentDto> comments;

    @Mock
    private DateBookingDto lastBooking;

    @Mock
    private DateBookingDto nextBooking;

    @Mock
    private ItemRequest request;

    @Mock
    private User owner;

    @InjectMocks
    private ItemMapper itemMapper;

    private ItemDto itemDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = new User();
        owner.setId(1L);
        owner.setName("Марк Пушкин");
        owner.setEmail("qwerty.111@example.com");

        request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Request Описание");

        item = new Item();
        item.setId(1L);
        item.setName("Item Название");
        item.setDescription("Item Описание");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(request);

        itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(UserDto.builder()
                        .id(owner.getId())
                        .name(owner.getName())
                        .email(owner.getEmail())
                        .build())
                .requestId(request.getId())
                .comments(comments)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();
    }

    @Test
    public void testToItemDto() {
        ItemDto result = itemMapper.toItemDto(item, comments, lastBooking, nextBooking);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(owner.getId(), result.getOwner().getId());
        assertEquals(owner.getName(), result.getOwner().getName());
        assertEquals(owner.getEmail(), result.getOwner().getEmail());
        assertEquals(comments, result.getComments());
        assertEquals(lastBooking, result.getLastBooking());
        assertEquals(nextBooking, result.getNextBooking());
        assertEquals(request.getId(), result.getRequestId());
    }

    @Test
    public void testToItemDtoWithoutComments() {
        ItemDto result = itemMapper.toItemDto(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(owner.getId(), result.getOwner().getId());
        assertEquals(owner.getName(), result.getOwner().getName());
        assertEquals(owner.getEmail(), result.getOwner().getEmail());
    }

    @Test
    public void testToItem() {
        Item result = itemMapper.toItem(itemDto, request);

        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
        assertEquals(itemDto.getOwner().getId(), result.getOwner().getId());
        assertEquals(itemDto.getOwner().getName(), result.getOwner().getName());
        assertEquals(itemDto.getOwner().getEmail(), result.getOwner().getEmail());
        assertEquals(request, result.getRequest());
    }

    @Test
    public void testToItemForRequestDto() {
        ItemForRequestDto result = itemMapper.toItemForRequestDto(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(item.getRequest().getId(), result.getRequestId());
    }
}