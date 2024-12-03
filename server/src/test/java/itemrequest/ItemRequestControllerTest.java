package itemrequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.request.controller.ItemRequestController;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.service.ItemRequestService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemRequestControllerTest {

    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController requestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
    }

    @Test
    public void testGetYourRequests() throws Exception {
        Long userId = 1L;
        List<ItemRequestDto> requests = List.of(ItemRequestDto.builder().id(1L).description("Request 1").build());

        when(requestService.getRequestsByUser(eq(userId))).thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"description\":\"Request 1\"}]"));
    }

    @Test
    public void testAddRequest() throws Exception {
        Long userId = 1L;
        ItemRequestDto requestDto = ItemRequestDto.builder().id(1L).description("Request 1").build();

        when(requestService.addRequest(eq(userId), eq(requestDto))).thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"description\":\"Request 1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"description\":\"Request 1\"}"));
    }

    @Test
    public void testGetAllRequests() throws Exception {
        Long userId = 1L;
        List<ItemRequestDto> requests = List.of(ItemRequestDto.builder().id(1L).description("Request 1").build());

        when(requestService.getAllRequests(eq(userId), any(Integer.class), any(Integer.class))).thenReturn(requests);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"description\":\"Request 1\"}]"));
    }

    @Test
    public void testGetRequestById() throws Exception {
        Long userId = 1L;
        Long requestId = 1L;
        ItemRequestDto request = ItemRequestDto.builder().id(requestId).description("Request 1").build();

        when(requestService.getRequestById(eq(requestId), eq(userId))).thenReturn(request);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"description\":\"Request 1\"}"));
    }
}