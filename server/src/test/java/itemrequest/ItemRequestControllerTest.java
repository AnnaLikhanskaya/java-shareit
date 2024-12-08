package itemrequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.ShareItServer;
import ru.practicum.request.controller.ItemRequestController;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.service.ItemRequestService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = ShareItServer.class)
@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
    }

    @Test
    public void testGetYourRequests() throws Exception {
        List<ItemRequestDto> requests = Collections.singletonList(ItemRequestDto.builder().id(1L).description("Request1").build());
        when(requestService.getRequestsByUser(anyLong())).thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Request1"));

        verify(requestService, times(1)).getRequestsByUser(anyLong());
    }

    @Test
    public void testAddRequest() throws Exception {
        ItemRequestDto requestDto = ItemRequestDto.builder().id(1L).description("NewRequest").build();
        when(requestService.addRequest(anyLong(), any(ItemRequestDto.class))).thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"NewRequest\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("NewRequest"));

        verify(requestService, times(1)).addRequest(anyLong(), any(ItemRequestDto.class));
    }

    @Test
    public void testGetAllRequests() throws Exception {
        List<ItemRequestDto> requests = Collections.singletonList(ItemRequestDto.builder().id(1L).description("Request1").build());
        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt())).thenReturn(requests);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Request1"));

        verify(requestService, times(1)).getAllRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    public void testGetRequestById() throws Exception {
        ItemRequestDto requestDto = ItemRequestDto.builder().id(1L).description("Request1").build();
        when(requestService.getRequestById(anyLong(), anyLong())).thenReturn(requestDto);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Request1"));

        verify(requestService, times(1)).getRequestById(anyLong(), anyLong());
    }

    @Test
    public void testGetYourRequests_NoRequests() throws Exception {
        System.out.println("Running testGetYourRequests_NoRequests");
        when(requestService.getRequestsByUser(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

        verify(requestService, times(1)).getRequestsByUser(anyLong());
    }


    @Test
    public void testGetAllRequests_NoRequests() throws Exception {
        System.out.println("Running testGetAllRequests_NoRequests");
        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

        verify(requestService, times(1)).getAllRequests(anyLong(), anyInt(), anyInt());
    }

}