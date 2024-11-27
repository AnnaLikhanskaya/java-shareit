package itemrequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.item.dto.ItemForRequestDto;
import ru.practicum.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ItemRequestDtoTest {

    @Test
    void testBuilder() {
        Long id = 1L;
        String description = "Test description";
        LocalDateTime created = LocalDateTime.now();
        List<ItemForRequestDto> items = List.of(new ItemForRequestDto(2L, "Item 1", "Item discription 1",
                true, 1L), new ItemForRequestDto(3L, "Item 2", "Item discription 2",
                true, 2L));

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(id)
                .description(description)
                .created(created)
                .items(items)
                .build();

        assertThat(itemRequestDto.getId()).isEqualTo(id);
        assertThat(itemRequestDto.getDescription()).isEqualTo(description);
        assertThat(itemRequestDto.getCreated()).isEqualTo(created);
        assertThat(itemRequestDto.getItems()).isEqualTo(items);
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String description = "Test description";
        LocalDateTime created = LocalDateTime.now();
        List<ItemForRequestDto> items = List.of(new ItemForRequestDto(2L, "Item 1", "Item discription 1",
                true, 1L), new ItemForRequestDto(3L, "Item 2", "Item discription 2",
                true, 2L));

        ItemRequestDto itemRequestDto = new ItemRequestDto(id, description, created, items);

        assertThat(itemRequestDto.getId()).isEqualTo(id);
        assertThat(itemRequestDto.getDescription()).isEqualTo(description);
        assertThat(itemRequestDto.getCreated()).isEqualTo(created);
        assertThat(itemRequestDto.getItems()).isEqualTo(items);
    }

    @Test
    void testNoArgsConstructor() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        assertThat(itemRequestDto.getId()).isNull();
        assertThat(itemRequestDto.getDescription()).isNull();
        assertThat(itemRequestDto.getCreated()).isNull();
        assertThat(itemRequestDto.getItems()).isNull();
    }
}
