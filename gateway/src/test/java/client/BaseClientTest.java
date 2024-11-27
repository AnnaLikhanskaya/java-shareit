package client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.client.BaseClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BaseClient baseClient;

    @Test
    void testGet() {
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Object> result = baseClient.get();

        assertThat(result).isEqualTo(responseEntity);
    }


    @Test
    void testGetWithUserId() {
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Object> result = baseClient.get(1L);

        assertThat(result).isEqualTo(responseEntity);
    }

    @Test
    void testGetWithPathAndUserId() {
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Object> result = baseClient.get("/items", 1L);

        assertThat(result).isEqualTo(responseEntity);
    }

    @Test
    void testPost() {
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Object> result = baseClient.post("/items", 1L, new Object());

        assertThat(result).isEqualTo(responseEntity);
    }

    @Test
    void testPatch() {
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Object> result = baseClient.patch("/items", 1L, new Object());

        assertThat(result).isEqualTo(responseEntity);
    }

    @Test
    void testDelete() {
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.DELETE), any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Object> result = baseClient.delete("/items", 1L);

        assertThat(result).isEqualTo(responseEntity);
    }

    @Test
    void testDefaultHeaders() {
        HttpHeaders headers = BaseClient.defaultHeaders(1L, true);

        assertThat(headers.getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(headers.getAccept()).containsExactly(MediaType.APPLICATION_JSON);
        assertThat(headers.get("X-Sharer-User-Id")).isEqualTo(List.of("1"));
    }

    @Test
    void testPrepareGatewayResponse() {
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        ResponseEntity<Object> result = BaseClient.prepareGatewayResponse(responseEntity);

        assertThat(result).isEqualTo(responseEntity);
    }

    @Test
    void testPrepareGatewayResponseWithError() {
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Object> result = BaseClient.prepareGatewayResponse(responseEntity);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}