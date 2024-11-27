package itemRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.request.controller.ItemRequestController;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.service.ItemRequestService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    @Test
    public void testGetYourRequests() {
        Long userId = 1L;
        List<ItemRequestDto> expectedRequests = Arrays.asList(new ItemRequestDto(), new ItemRequestDto());

        when(requestService.getRequestsByUser(eq(userId))).thenReturn(expectedRequests);

        List<ItemRequestDto> actualRequests = itemRequestController.getYourRequests(userId);

        assertEquals(expectedRequests, actualRequests);
    }

    @Test
    public void testAddRequest() {
        Long userId = 1L;
        ItemRequestDto requestDto = new ItemRequestDto();
        ItemRequestDto expectedResponse = new ItemRequestDto();

        when(requestService.addRequest(eq(userId), eq(requestDto))).thenReturn(expectedResponse);

        ItemRequestDto actualResponse = itemRequestController.addRequest(userId, requestDto);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetAllRequests() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        List<ItemRequestDto> expectedRequests = Arrays.asList(new ItemRequestDto(), new ItemRequestDto());

        when(requestService.getAllRequests(eq(userId), eq(from), eq(size))).thenReturn(expectedRequests);

        List<ItemRequestDto> actualRequests = itemRequestController.getAllRequests(userId, from, size);

        assertEquals(expectedRequests, actualRequests);
    }

    @Test
    public void testGetRequestById() {
        Long userId = 1L;
        Long requestId = 1L;
        ItemRequestDto expectedResponse = new ItemRequestDto();

        when(requestService.getRequestById(eq(requestId), eq(userId))).thenReturn(expectedResponse);

        ItemRequestDto actualResponse = itemRequestController.getRequestById(userId, requestId);

        assertEquals(expectedResponse, actualResponse);
    }
}