package user;

import org.junit.jupiter.api.Test;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMapperTest {

    @Test
    public void testToUserDto() {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        UserDto userDto = UserMapper.toUserDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    public void testToUser() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        User user = UserMapper.toUser(userDto);

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }
}