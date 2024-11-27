package user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserDtoTest {

    @Test
    void testBuilder() {
        Long id = 1L;
        String name = "Test Name";
        String email = "test@example.com";

        UserDto userDto = UserDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();

        assertThat(userDto.getId()).isEqualTo(id);
        assertThat(userDto.getName()).isEqualTo(name);
        assertThat(userDto.getEmail()).isEqualTo(email);
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String name = "Test Name";
        String email = "test@example.com";

        UserDto userDto = new UserDto(id, name, email);

        assertThat(userDto.getId()).isEqualTo(id);
        assertThat(userDto.getName()).isEqualTo(name);
        assertThat(userDto.getEmail()).isEqualTo(email);
    }

    @Test
    void testNoArgsConstructor() {
        UserDto userDto = new UserDto();

        assertThat(userDto.getId()).isNull();
        assertThat(userDto.getName()).isNull();
        assertThat(userDto.getEmail()).isNull();
    }

}