//package itemRequest;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import ru.practicum.item.storage.ItemRepository;
//import ru.practicum.request.model.ItemRequest;
//import ru.practicum.request.storage.ItemRequestRepository;
//import ru.practicum.user.model.User;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@ContextConfiguration(classes = ItemRepository.class) //
//public class ItemRequestRepositoryTest {
//
//    @Autowired
//    private ItemRequestRepository itemRequestRepository;
//
//    private User user1;
//    private User user2;
//    private ItemRequest request1;
//    private ItemRequest request2;
//    private ItemRequest request3;
//
//    @BeforeEach
//    public void setUp() {
//        user1 = User.builder()
//                .id(1L)
//                .name("User 1")
//                .email("user1@example.com")
//                .build();
//
//        user2 = User.builder()
//                .id(2L)
//                .name("User 2")
//                .email("user2@example.com")
//                .build();
//
//        request1 = ItemRequest.builder()
//                .id(1L)
//                .description("Request 1")
//                .created(LocalDateTime.now())
//                .requestor(user1)
//                .build();
//
//        request2 = ItemRequest.builder()
//                .id(2L)
//                .description("Request 2")
//                .created(LocalDateTime.now())
//                .requestor(user1)
//                .build();
//
//        request3 = ItemRequest.builder()
//                .id(3L)
//                .description("Request 3")
//                .created(LocalDateTime.now())
//                .requestor(user2)
//                .build();
//
//        itemRequestRepository.save(request1);
//        itemRequestRepository.save(request2);
//        itemRequestRepository.save(request3);
//    }
//
//    @Test
//    public void testFindByRequestor() {
//        List<ItemRequest> requests = itemRequestRepository.findByRequestor(user1);
//
//        assertEquals(2, requests.size());
//        assertEquals(request1.getId(), requests.get(0).getId());
//        assertEquals(request2.getId(), requests.get(1).getId());
//    }
//
//    @Test
//    public void testFindByRequestorNot() {
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("created").descending());
//        List<ItemRequest> requests = itemRequestRepository.findByRequestorNot(user1, pageable);
//
//        assertEquals(1, requests.size());
//        assertEquals(request3.getId(), requests.get(0).getId());
//    }
//}