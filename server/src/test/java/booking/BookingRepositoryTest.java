//package booking;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.ContextConfiguration;
//import ru.practicum.ShareItServer;
//import ru.practicum.booking.model.Booking;
//import ru.practicum.booking.storage.BookingRepository;
//import ru.practicum.item.model.Item;
//import ru.practicum.item.paging.CustomPageRequest;
//import ru.practicum.item.storage.ItemRepository;
//import ru.practicum.user.model.User;
//import ru.practicum.user.storage.UserRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static ru.practicum.booking.dto.BookingStatus.REJECTED;
//import static ru.practicum.booking.dto.BookingStatus.WAITING;
//
//@DataJpaTest
//@ContextConfiguration(classes = ShareItServer.class)
//public class BookingRepositoryTest {
//    @Autowired
//    private BookingRepository bookingRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private ItemRepository itemRepository;
//    private User firstUser;
//    private User secondUser;
//    private Item firstItem;
//    private Item secondItem;
//    private Booking firstBooking;
//    private Booking secondBooking;
//    private Booking thirdBooking;
//    private Pageable pageable;
//
//    @BeforeEach
//    public void beforeEach() {
//        firstUser = userRepository.save(new User(1L, "Имя первого", "first@email.com"));
//        secondUser = userRepository.save(new User(2L, "Имя второго", "second@email.com"));
//        firstItem = itemRepository.save(new Item(
//                1L,
//                "Название первого",
//                "Описание первого",
//                true,
//                secondUser,
//                null
//        ));
//        secondItem = itemRepository.save(new Item(
//                2L,
//                "Название второго",
//                "Описание второго",
//                true,
//                firstUser,
//                null
//        ));
//        firstBooking = bookingRepository.save(new Booking(
//                1L,
//                LocalDateTime.of(2022, 1, 1, 1, 1, 1),
//                LocalDateTime.of(2022, 1, 3, 1, 1, 1),
//                firstItem,
//                firstUser,
//                REJECTED
//        ));
//        secondBooking = bookingRepository.save(new Booking(
//                2L,
//                LocalDateTime.of(2024, 1, 1, 1, 1, 1),
//                LocalDateTime.of(2024, 1, 3, 1, 1, 1),
//                secondItem,
//                firstUser,
//                WAITING
//        ));
//        thirdBooking = bookingRepository.save(new Booking(
//                3L,
//                LocalDateTime.of(2022, 1, 1, 1, 1, 1),
//                LocalDateTime.of(2024, 1, 3, 1, 1, 1),
//                secondItem,
//                firstUser,
//                WAITING
//        ));
//        pageable = CustomPageRequest.create(0, 10);
//    }
//
//    @AfterEach
//    public void afterEach() {
//        bookingRepository.deleteAll();
//        itemRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @Test
//    void findByBooker() {
//        List<Booking> result = bookingRepository.findByBooker(firstUser, pageable);
//        assertThat(result).containsExactlyInAnyOrder(firstBooking, secondBooking, thirdBooking);
//    }
//
//    @Test
//    void findByBookerAndEndIsBefore() {
//        List<Booking> result = bookingRepository.findByBookerAndEndIsBefore(firstUser, LocalDateTime.now(), pageable);
//        assertThat(result).containsExactlyInAnyOrder(firstBooking);
//        assertThat(result).doesNotContain(secondBooking, thirdBooking);
//    }
//
//    @Test
//    void findByBookerAndStartIsAfter() {
//        List<Booking> result = bookingRepository.findByBookerAndStartIsAfter(firstUser, LocalDateTime.now(), pageable);
//        assertThat(result).containsExactlyInAnyOrder(secondBooking);
//        assertThat(result).doesNotContain(firstBooking, thirdBooking);
//    }
//
////    @Test
////    void findCurrentByBooker() {
////        List<Booking> result = bookingRepository.findCurrentByBooker(firstUser, pageable);
////        assertThat(result).containsExactlyInAnyOrder(thirdBooking);
////        assertThat(result).doesNotContain(secondBooking, firstBooking);
////    }
//
//    @Test
//    void findByBookerAndStatus() {
//        List<Booking> result = bookingRepository.findByBookerAndStatus(firstUser, WAITING, pageable);
//        assertThat(result).containsExactlyInAnyOrder(thirdBooking, secondBooking);
//        assertThat(result).doesNotContain(firstBooking);
//    }
//
//    @Test
//    void findByBookerAndItem() {
//        List<Booking> result = bookingRepository.findByBookerAndItem(firstUser, firstItem);
//        assertThat(result).containsExactlyInAnyOrder(firstBooking);
//        assertThat(result).doesNotContain(secondBooking, thirdBooking);
//    }
//
//    @Test
//    void findByOwner() {
//        List<Booking> result = bookingRepository.findByOwner(secondUser, pageable);
//        assertThat(result).containsExactlyInAnyOrder(firstBooking);
//        assertThat(result).doesNotContain(secondBooking, thirdBooking);
//    }
//
//    @Test
//    void findByOwnerAndEndIsBefore() {
//        List<Booking> result = bookingRepository.findByOwnerAndEndIsBefore(secondUser, LocalDateTime.now(), pageable);
//        assertThat(result).containsExactlyInAnyOrder(firstBooking);
//        assertThat(result).doesNotContain(secondBooking, thirdBooking);
//    }
//
////    @Test
////    void findByOwnerAndStartIsAfter() {
////        List<Booking> result = bookingRepository.findByOwnerAndStartIsAfter(firstUser, LocalDateTime.now(), pageable);
////        assertThat(result).containsExactlyInAnyOrder(secondBooking);
////        assertThat(result).doesNotContain(firstBooking, thirdBooking);
////    }
//
////    @Test
////    void findCurrentByOwner() {
////        List<Booking> result = bookingRepository.findCurrentByOwner(firstUser, pageable);
////        assertThat(result).containsExactlyInAnyOrder(thirdBooking);
////        assertThat(result).doesNotContain(secondBooking, firstBooking);
////    }
//
//    @Test
//    void findByOwnerAndStatus() {
//        List<Booking> result = bookingRepository.findByOwnerAndStatus(secondUser, REJECTED, pageable);
//        assertThat(result).containsExactlyInAnyOrder(firstBooking);
//        assertThat(result).doesNotContain(secondBooking, thirdBooking);
//    }
//
//    @Test
//    void findByItemAndEndIsBefore() {
//        List<Booking> result = bookingRepository.findByItemAndEndIsBefore(firstItem, LocalDateTime.now());
//        assertThat(result).containsExactlyInAnyOrder(firstBooking);
//        assertThat(result).doesNotContain(secondBooking, thirdBooking);
//    }
//
////    @Test
////    void findByItemAndStartIsAfter() {
////        List<Booking> result = bookingRepository.findByItemAndStartIsAfter(secondItem, LocalDateTime.now());
////        assertThat(result).containsExactlyInAnyOrder(secondBooking);
////        assertThat(result).doesNotContain(firstBooking, thirdBooking);
////    }
//}