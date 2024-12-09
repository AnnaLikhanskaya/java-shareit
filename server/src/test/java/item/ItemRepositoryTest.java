package item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.item.model.Item;
import ru.practicum.item.service.ItemServiceDb;
import ru.practicum.item.storage.ItemRepository;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ItemRepositoryTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceDb itemServiceDb;

    private User user;
    private Item item;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        item = new Item();
        item.setId(1L);
        item.setName("Item Name");
        item.setDescription("Item Description");
        item.setAvailable(true);
        item.setOwner(user);
    }

    @Test
    public void testFindByOwner() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        when(itemRepository.findByOwner(any(User.class), any(Pageable.class))).thenReturn(List.of(item));

        List<Item> result = itemRepository.findByOwner(user, pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item, result.get(0));
    }


    @Test
    public void testSearch() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        when(itemRepository.search(anyString(), any(Pageable.class))).thenReturn(List.of(item));

        List<Item> result = itemRepository.search("item", pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item, result.get(0));
    }

    @Test
    public void testFindByRequest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Request Description");
        itemRequest.setCreated(java.time.LocalDateTime.now());

        when(itemRepository.findByRequest(any(ItemRequest.class))).thenReturn(List.of(item));

        List<Item> result = itemRepository.findByRequest(itemRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item, result.get(0));
    }
}
