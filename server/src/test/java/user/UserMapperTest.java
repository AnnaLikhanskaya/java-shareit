package user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServer;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ContextConfiguration(classes = ShareItServer.class)
@DataJpaTest
public class UserMapperTest {

    @Test
    public void testToUserDto_AllFields() {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .name("John Doe")
                .build();

        UserDto userDto = UserMapper.toUserDto(user);

        assertEquals(1L, userDto.getId());
        assertEquals("user@example.com", userDto.getEmail());
        assertEquals("John Doe", userDto.getName());
    }

    @Test
    public void testToUserDto_NullFields() {
        User user = User.builder()
                .id(null)
                .email(null)
                .name(null)
                .build();

        UserDto userDto = UserMapper.toUserDto(user);

        assertNull(userDto.getId());
        assertNull(userDto.getEmail());
        assertNull(userDto.getName());
    }

    @Test
    public void testToUserDto_EmptyFields() {
        User user = User.builder()
                .id(0L)
                .email("")
                .name("")
                .build();

        UserDto userDto = UserMapper.toUserDto(user);

        assertEquals(0L, userDto.getId());
        assertEquals("", userDto.getEmail());
        assertEquals("", userDto.getName());
    }

    @Test
    public void testToUser_AllFields() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("user@example.com")
                .name("John Doe")
                .build();

        User user = UserMapper.toUser(userDto);

        assertEquals(1L, user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("John Doe", user.getName());
    }

    @Test
    public void testToUser_NullFields() {
        UserDto userDto = UserDto.builder()
                .id(null)
                .email(null)
                .name(null)
                .build();

        User user = UserMapper.toUser(userDto);

        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getName());
    }

    @Test
    public void testToUser_EmptyFields() {
        UserDto userDto = UserDto.builder()
                .id(0L)
                .email("")
                .name("")
                .build();

        User user = UserMapper.toUser(userDto);

        assertEquals(0L, user.getId());
        assertEquals("", user.getEmail());
        assertEquals("", user.getName());
    }
}