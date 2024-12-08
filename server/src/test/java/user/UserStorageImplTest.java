//package user;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.practicum.exception.DuplicateException;
//import ru.practicum.exception.NotExsistObject;
//import ru.practicum.user.model.User;
//import ru.practicum.user.storage.UserStorageImpl;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserStorageImplTest {
//
//    @InjectMocks
//    private UserStorageImpl userStorage;
//
//    @BeforeEach
//    public void setUp() {
//        // Инициализация перед каждым тестом
//    }
//
//    @Test
//    public void testGetUsers() {
//        User user1 = User.builder().id(1L).name("User1").email("user1@example.com").build();
//        User user2 = User.builder().id(2L).name("User2").email("user2@example.com").build();
//
//        userStorage.addUser(user1);
//        userStorage.addUser(user2);
//
//        List<User> users = userStorage.getUsers();
//
//        assertEquals(2, users.size());
//        assertTrue(users.contains(user1));
//        assertTrue(users.contains(user2));
//    }
//
//    @Test
//    public void testGetUserById() {
//        User user = User.builder().id(1L).name("User1").email("user1@example.com").build();
//        userStorage.addUser(user);
//
//        User retrievedUser = userStorage.getUserById(1L);
//
//        assertNotNull(retrievedUser);
//        assertEquals(user.getId(), retrievedUser.getId());
//        assertEquals(user.getName(), retrievedUser.getName());
//        assertEquals(user.getEmail(), retrievedUser.getEmail());
//    }
//
//    @Test
//    public void testGetUserById_UserNotFound() {
//        assertThrows(NotExsistObject.class, () -> userStorage.getUserById(1L));
//    }
//
//    @Test
//    public void testAddUser() {
//        User user = User.builder().name("User1").email("user1@example.com").build();
//
//        User addedUser = userStorage.addUser(user);
//
//        assertNotNull(addedUser);
//        assertEquals(1L, addedUser.getId());
//        assertEquals("User1", addedUser.getName());
//        assertEquals("user1@example.com", addedUser.getEmail());
//    }
//
//    @Test
//    public void testAddUser_DuplicateEmail() {
//        User user1 = User.builder().name("User1").email("user1@example.com").build();
//        User user2 = User.builder().name("User2").email("user1@example.com").build();
//
//        userStorage.addUser(user1);
//
//        assertThrows(DuplicateException.class, () -> userStorage.addUser(user2));
//    }
//
//    @Test
//    public void testUpdateUser() {
//        User user = User.builder().name("User1").email("user1@example.com").build();
//        userStorage.addUser(user);
//
//        User updatedUser = User.builder().id(1L).name("UpdatedUser").email("updated@example.com").build();
//        User result = userStorage.updateUser(updatedUser);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//        assertEquals("UpdatedUser", result.getName());
//        assertEquals("updated@example.com", result.getEmail());
//    }
//
//    @Test
//    public void testUpdateUser_UserNotFound() {
//        User user = User.builder().id(1L).name("User1").email("user1@example.com").build();
//
//        assertThrows(NotExsistObject.class, () -> userStorage.updateUser(user));
//    }
//
//    @Test
//    public void testUpdateUser_DuplicateEmail() {
//        User user1 = User.builder().name("User1").email("user1@example.com").build();
//        User user2 = User.builder().name("User2").email("user2@example.com").build();
//
//        userStorage.addUser(user1);
//        userStorage.addUser(user2);
//
//        User updatedUser = User.builder().id(1L).name("User1").email("user2@example.com").build();
//
//        assertThrows(DuplicateException.class, () -> userStorage.updateUser(updatedUser));
//    }
//
//    @Test
//    public void testDeleteUser() {
//        User user = User.builder().name("User1").email("user1@example.com").build();
//        userStorage.addUser(user);
//
//        userStorage.deleteUser(1L);
//
//        assertThrows(NotExsistObject.class, () -> userStorage.getUserById(1L));
//    }
//
//    @Test
//    public void testDeleteUser_UserNotFound() {
//        assertThrows(NotExsistObject.class, () -> userStorage.deleteUser(1L));
//    }
//}
//
