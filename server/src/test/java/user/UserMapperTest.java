package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServer;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = ShareItServer.class)
@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToUserDto() {
        User user = User.builder()
                .id(1L)
                .name("UserName")
                .email("user@example.com")
                .build();

        UserDto userDto = UserMapper.toUserDto(user);

        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals("UserName", userDto.getName());
        assertEquals("user@example.com", userDto.getEmail());
    }

    @Test
    public void testToUser() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("UserName")
                .email("user@example.com")
                .build();

        User user = UserMapper.toUser(userDto);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("UserName", user.getName());
        assertEquals("user@example.com", user.getEmail());
    }
}