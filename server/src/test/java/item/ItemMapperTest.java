package item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServer;
import ru.practicum.booking.dto.DateBookingDto;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemForRequestDto;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = ShareItServer.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ItemMapperTest {

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToItemDto() {
        Item item = Item.builder()
                .id(1L)
                .name("ItemName")
                .description("ItemDescription")
                .available(true)
                .owner(User.builder().id(1L).name("OwnerName").build())
                .request(ItemRequest.builder().id(2L).build())
                .build();

        List<CommentDto> comments = Collections.emptyList();
        DateBookingDto lastBooking = null;
        DateBookingDto nextBooking = null;

        ItemDto itemDto = ItemMapper.toItemDto(item, comments, lastBooking, nextBooking);

        assertNotNull(itemDto);
        assertEquals(1L, itemDto.getId());
        assertEquals("ItemName", itemDto.getName());
        assertEquals("ItemDescription", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());
        assertNotNull(itemDto.getOwner());
        assertEquals(1L, itemDto.getOwner().getId());
        assertEquals("OwnerName", itemDto.getOwner().getName());
        assertEquals(2L, itemDto.getRequestId());
        assertEquals(comments, itemDto.getComments());
        assertEquals(lastBooking, itemDto.getLastBooking());
        assertEquals(nextBooking, itemDto.getNextBooking());
    }

    @Test
    public void testToItemDtoWithoutComments() {
        Item item = Item.builder()
                .id(1L)
                .name("ItemName")
                .description("ItemDescription")
                .available(true)
                .owner(User.builder().id(1L).name("OwnerName").build())
                .request(ItemRequest.builder().id(2L).build())
                .build();

        ItemDto itemDto = ItemMapper.toItemDto(item);

        assertNotNull(itemDto);
        assertEquals(1L, itemDto.getId());
        assertEquals("ItemName", itemDto.getName());
        assertEquals("ItemDescription", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());
        assertNotNull(itemDto.getOwner());
        assertEquals(1L, itemDto.getOwner().getId());
        assertEquals("OwnerName", itemDto.getOwner().getName());
        assertEquals(2L, itemDto.getRequestId());
    }

    @Test
    public void testToItemDtoWithComments() {
        Item item = Item.builder()
                .id(1L)
                .name("ItemName")
                .description("ItemDescription")
                .available(true)
                .owner(User.builder().id(1L).name("OwnerName").build())
                .request(ItemRequest.builder().id(2L).build())
                .build();

        List<CommentDto> comments = Collections.emptyList();

        ItemDto itemDto = ItemMapper.toItemDto(item, comments);

        assertNotNull(itemDto);
        assertEquals(1L, itemDto.getId());
        assertEquals("ItemName", itemDto.getName());
        assertEquals("ItemDescription", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());
        assertNotNull(itemDto.getOwner());
        assertEquals(1L, itemDto.getOwner().getId());
        assertEquals("OwnerName", itemDto.getOwner().getName());
        assertEquals(2L, itemDto.getRequestId());
        assertEquals(comments, itemDto.getComments());
    }

    @Test
    public void testToItem() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("ItemName")
                .description("ItemDescription")
                .available(true)
                .owner(UserDto.builder().id(1L).name("OwnerName").build())
                .build();

        ItemRequest request = ItemRequest.builder().id(2L).build();

        Item item = ItemMapper.toItem(itemDto, request);

        assertNotNull(item);
        assertEquals(1L, item.getId());
        assertEquals("ItemName", item.getName());
        assertEquals("ItemDescription", item.getDescription());
        assertEquals(true, item.getAvailable());
        assertNotNull(item.getOwner());
        assertEquals(1L, item.getOwner().getId());
        assertEquals("OwnerName", item.getOwner().getName());
        assertEquals(request, item.getRequest());
    }

    @Test
    public void testToItemForRequestDto() {
        Item item = Item.builder()
                .id(1L)
                .name("ItemName")
                .description("ItemDescription")
                .available(true)
                .request(ItemRequest.builder().id(2L).build())
                .build();

        ItemForRequestDto itemForRequestDto = ItemMapper.toItemForRequestDto(item);

        assertNotNull(itemForRequestDto);
        assertEquals(1L, itemForRequestDto.getId());
        assertEquals("ItemName", itemForRequestDto.getName());
        assertEquals("ItemDescription", itemForRequestDto.getDescription());
        assertEquals(true, itemForRequestDto.getAvailable());
        assertEquals(2L, itemForRequestDto.getRequestId());
    }
}