package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.NotExsistObject;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserStorageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserStorageImplTest {
    private UserStorageImpl userStorage;

    @BeforeEach
    public void setUp() {
        userStorage = new UserStorageImpl();
    }

    @Test
    public void testGetUsers() {
        User user1 = User.builder().id(1L).name("User 1").email("user1@example.com").build();
        User user2 = User.builder().id(2L).name("User 2").email("user2@example.com").build();

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        List<User> users = userStorage.getUsers();

        assertEquals(2, users.size());
        assertEquals(user1.getId(), users.get(0).getId());
        assertEquals(user2.getId(), users.get(1).getId());
    }

    @Test
    public void testGetUserById() {
        User user = User.builder().id(1L).name("User 1").email("user1@example.com").build();
        userStorage.addUser(user);

        User retrievedUser = userStorage.getUserById(1L);

        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getName(), retrievedUser.getName());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
    }

    @Test
    public void testGetUserByIdNotFound() {
        assertThrows(NotExsistObject.class, () -> userStorage.getUserById(1L));
    }

    @Test
    public void testAddUser() {
        User user = User.builder().name("New User").email("newuser@example.com").build();

        User addedUser = userStorage.addUser(user);

        assertEquals(1L, addedUser.getId());
        assertEquals(user.getName(), addedUser.getName());
        assertEquals(user.getEmail(), addedUser.getEmail());
    }

    @Test
    public void testAddUserDuplicate() {
        User user1 = User.builder().name("User 1").email("user1@example.com").build();
        User user2 = User.builder().name("User 2").email("user1@example.com").build();

        userStorage.addUser(user1);

        assertThrows(DuplicateException.class, () -> userStorage.addUser(user2));
    }

    @Test
    public void testUpdateUser() {
        User user = User.builder().name("User 1").email("user1@example.com").build();
        userStorage.addUser(user);

        User updatedUser = User.builder().id(1L).name("Updated User").email("updateduser@example.com").build();
        User result = userStorage.updateUser(updatedUser);

        assertEquals(updatedUser.getId(), result.getId());
        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
    }

    @Test
    public void testUpdateUserNotFound() {
        User user = User.builder().id(1L).name("User 1").email("user1@example.com").build();

        assertThrows(NotExsistObject.class, () -> userStorage.updateUser(user));
    }

    @Test
    public void testUpdateUserDuplicateEmail() {
        User user1 = User.builder().name("User 1").email("user1@example.com").build();
        User user2 = User.builder().name("User 2").email("user2@example.com").build();

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        User updatedUser = User.builder().id(1L).name("User 1").email("user2@example.com").build();

        assertThrows(DuplicateException.class, () -> userStorage.updateUser(updatedUser));
    }

    @Test
    public void testDeleteUser() {
        User user = User.builder().name("User 1").email("user1@example.com").build();
        userStorage.addUser(user);

        userStorage.deleteUser(1L);

        assertThrows(NotExsistObject.class, () -> userStorage.getUserById(1L));
    }

    @Test
    public void testDeleteUserNotFound() {
        assertThrows(NotExsistObject.class, () -> userStorage.deleteUser(1L));
    }

    @Test
    public void testCheckUserAvailability() {
        User user = User.builder().name("User 1").email("user1@example.com").build();
        userStorage.addUser(user);

        User retrievedUser = userStorage.checkUserAvailability("найти", 1L);

        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getName(), retrievedUser.getName());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
    }

    @Test
    public void testCheckUserAvailabilityNotFound() {
        assertThrows(NotExsistObject.class, () -> userStorage.checkUserAvailability("найти", 1L));
    }

}

