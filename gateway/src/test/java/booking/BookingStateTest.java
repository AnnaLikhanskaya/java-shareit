package booking;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.booking.dto.BookingState;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = ShareItGateway.class)
public class BookingStateTest {

    @Test
    public void testFromValidState() {
        Optional<BookingState> result = BookingState.from("CURRENT");
        assertTrue(result.isPresent());
        assertEquals(BookingState.CURRENT, result.get());
    }

    @Test
    public void testFromInvalidState() {
        Optional<BookingState> result = BookingState.from("INVALID_STATE");
        assertFalse(result.isPresent());
    }

    @Test
    public void testFromCaseInsensitive() {
        Optional<BookingState> result = BookingState.from("current");
        assertTrue(result.isPresent());
        assertEquals(BookingState.CURRENT, result.get());
    }

    @Test
    public void testFromAllStates() {
        for (BookingState state : BookingState.values()) {
            Optional<BookingState> result = BookingState.from(state.name());
            assertTrue(result.isPresent());
            assertEquals(state, result.get());
        }
    }

    @Test
    public void testFromIncorrectState() {
        Optional<BookingState> result = BookingState.from("INCORRECT");
        assertTrue(result.isPresent());
        assertEquals(BookingState.INCORRECT, result.get());
    }
}