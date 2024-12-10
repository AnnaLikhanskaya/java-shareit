package itemrequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exception.NotExsistObject;
import ru.practicum.item.dto.ItemForRequestDto;
import ru.practicum.item.storage.ItemRepository;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.service.ItemRequestServiceImpl;
import ru.practicum.request.storage.ItemRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static ru.practicum.request.mapper.ItemRequestMapper.toItemRequest;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    public void testAddRequest() {
        Long userId = 1L;
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .description("Test Description")
                .build();

        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .build();

        ItemRequest itemRequest = toItemRequest(requestDto, user);
        itemRequest.setCreated(LocalDateTime.now());

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto result = itemRequestService.addRequest(userId, requestDto);

        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemRequest.getCreated(), result.getCreated());
    }

    @Test
    public void testGetRequestById() {
        Long userId = 1L;
        Long requestId = 1L;

        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .build();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(requestId)
                .description("Test Description")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();

        List<ItemForRequestDto> answers = Arrays.asList();


        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));
        when(requestRepository.findById(eq(requestId))).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequest(eq(itemRequest))).thenReturn(Arrays.asList());

        ItemRequestDto result = itemRequestService.getRequestById(requestId, userId);

        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemRequest.getCreated(), result.getCreated());
        assertEquals(answers, result.getItems());
    }

    @Test
    public void testGetAllRequests() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;

        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .build();

        ItemRequest itemRequest1 = ItemRequest.builder()
                .id(1L)
                .description("Test Description 1")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();

        ItemRequest itemRequest2 = ItemRequest.builder()
                .id(2L)
                .description("Test Description 2")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();

        List<ItemRequest> itemRequests = Arrays.asList(itemRequest1, itemRequest2);

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));
        when(requestRepository.findByRequestorNot(eq(user), any())).thenReturn(itemRequests);
        when(itemRepository.findByRequestIds(any())).thenReturn(Arrays.asList());

        List<ItemRequestDto> result = itemRequestService.getAllRequests(userId, from, size);

        assertEquals(2, result.size());
        assertEquals(itemRequest1.getId(), result.get(0).getId());
        assertEquals(itemRequest2.getId(), result.get(1).getId());
    }

    @Test
    public void testGetRequestsByUser() {
        Long userId = 1L;

        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .build();

        ItemRequest itemRequest1 = ItemRequest.builder()
                .id(1L)
                .description("Test Description 1")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();

        ItemRequest itemRequest2 = ItemRequest.builder()
                .id(2L)
                .description("Test Description 2")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();

        List<ItemRequest> itemRequests = Arrays.asList(itemRequest1, itemRequest2);

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));
        when(requestRepository.findByRequestor(eq(user))).thenReturn(itemRequests);
        when(itemRepository.findByRequestIds(any())).thenReturn(Arrays.asList(
        ));

        List<ItemRequestDto> result = itemRequestService.getRequestsByUser(userId);

        assertEquals(2, result.size());
        assertEquals(itemRequest1.getId(), result.get(0).getId());
        assertEquals(itemRequest2.getId(), result.get(1).getId());
    }

    @Test
    public void testGetUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(eq(userId))).thenReturn(Optional.empty());

        assertThrows(NotExsistObject.class, () -> itemRequestService.getRequestById(1L, 1L));
    }

}