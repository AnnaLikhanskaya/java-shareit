package item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.booking.dto.DateBookingDto;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemDtoTest {

    @Mock
    private UserDto owner;

    @Mock
    private List<CommentDto> comments;

    @Mock
    private DateBookingDto lastBooking;

    @Mock
    private DateBookingDto nextBooking;

    @InjectMocks
    private ItemDto itemDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        itemDto = ItemDto.builder()
                .id(1L)
                .name("Item Name")
                .description("Item Description")
                .available(true)
                .owner(owner)
                .comments(comments)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .requestId(1L)
                .build();
    }

    @Test
    public void testItemDtoBuilder() {
        assertNotNull(itemDto);
        assertEquals(1L, itemDto.getId());
        assertEquals("Item Name", itemDto.getName());
        assertEquals("Item Description", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());
        assertEquals(owner, itemDto.getOwner());
        assertEquals(comments, itemDto.getComments());
        assertEquals(lastBooking, itemDto.getLastBooking());
        assertEquals(nextBooking, itemDto.getNextBooking());
        assertEquals(1L, itemDto.getRequestId());
    }

    @Test
    public void testItemDtoNoArgsConstructor() {
        ItemDto noArgsItemDto = new ItemDto();
        assertNotNull(noArgsItemDto);
    }

    @Test
    public void testItemDtoAllArgsConstructor() {
        ItemDto allArgsItemDto = new ItemDto(1L, "Item Name", "Item Description",
                true, owner, comments, lastBooking, nextBooking, 1L);
        assertNotNull(allArgsItemDto);
        assertEquals(1L, allArgsItemDto.getId());
        assertEquals("Item Name", allArgsItemDto.getName());
        assertEquals("Item Description", allArgsItemDto.getDescription());
        assertEquals(true, allArgsItemDto.getAvailable());
        assertEquals(owner, allArgsItemDto.getOwner());
        assertEquals(comments, allArgsItemDto.getComments());
        assertEquals(lastBooking, allArgsItemDto.getLastBooking());
        assertEquals(nextBooking, allArgsItemDto.getNextBooking());
        assertEquals(1L, allArgsItemDto.getRequestId());
    }
}