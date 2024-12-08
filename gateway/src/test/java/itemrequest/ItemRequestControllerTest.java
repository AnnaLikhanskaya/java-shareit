package itemrequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.request.client.RequestClient;
import ru.practicum.request.controller.ItemRequestController;
import ru.practicum.request.dto.ItemRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = ShareItGateway.class)
@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    @Mock
    private RequestClient requestClient;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private Long userId;
    private Long requestId;
    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        userId = 1L;
        requestId = 1L;
        requestDto = new ItemRequestDto();
    }

    @Test
    void getYourRequests_shouldReturnRequests() {
        when(requestClient.getRequestsByUser(userId)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemRequestController.getYourRequests(userId);

        verify(requestClient, times(1)).getRequestsByUser(userId);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void addRequest_shouldAddRequest() {
        when(requestClient.addRequest(requestDto, userId)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemRequestController.addRequest(userId, requestDto);

        verify(requestClient, times(1)).addRequest(requestDto, userId);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void getAllRequests_shouldReturnAllRequests() {
        Integer from = 0;
        Integer size = 10;
        when(requestClient.getAllRequests(userId, from, size)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemRequestController.getAllRequests(userId, from, size);

        verify(requestClient, times(1)).getAllRequests(userId, from, size);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void getRequestById_shouldReturnRequest() {
        when(requestClient.getRequestById(requestId, userId)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemRequestController.getRequestById(userId, requestId);

        verify(requestClient, times(1)).getRequestById(requestId, userId);
        assertEquals(ResponseEntity.ok().build(), response);
    }
}