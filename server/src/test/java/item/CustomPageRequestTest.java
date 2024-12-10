package item;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import ru.practicum.exception.InvalidRequestException;
import ru.practicum.item.paging.CustomPageRequest;

import static org.junit.jupiter.api.Assertions.*;

class CustomPageRequestTest {

    @Test
    void testCreateWithValidParameters() {
        CustomPageRequest pageRequest = (CustomPageRequest) CustomPageRequest.create(10, 20);
        assertEquals(10, pageRequest.getOffset());
        assertEquals(20, pageRequest.getPageSize());
        assertEquals(Sort.unsorted(), pageRequest.getSort());
    }

    @Test
    void testCreateWithSort() {
        Sort sort = Sort.by("name").ascending();
        CustomPageRequest pageRequest = (CustomPageRequest) CustomPageRequest.create(10, 20, sort);
        assertEquals(10, pageRequest.getOffset());
        assertEquals(20, pageRequest.getPageSize());
        assertEquals(sort, pageRequest.getSort());
    }

    @Test
    void testCreateUnpaged() {
        Sort sort = Sort.by("name").ascending();
        CustomPageRequest pageRequest = (CustomPageRequest) CustomPageRequest.unpaged(sort);
        assertEquals(0, pageRequest.getOffset());
        assertEquals(Integer.MAX_VALUE, pageRequest.getPageSize());
        assertEquals(sort, pageRequest.getSort());
    }

    @Test
    void testCreateWithNullParameters() {
        CustomPageRequest pageRequest = (CustomPageRequest) CustomPageRequest.create(null, null);
        assertEquals(0, pageRequest.getOffset());
        assertEquals(Integer.MAX_VALUE, pageRequest.getPageSize());
        assertEquals(Sort.unsorted(), pageRequest.getSort());
    }

    @Test
    void testCreateWithInvalidParameters() {
        assertThrows(InvalidRequestException.class, () -> CustomPageRequest.create(10, null));
        assertThrows(InvalidRequestException.class, () -> CustomPageRequest.create(null, 20));
        assertThrows(InvalidRequestException.class, () -> CustomPageRequest.create(10, 0));
        assertThrows(InvalidRequestException.class, () -> CustomPageRequest.create(-1, 20));
    }

    @Test
    void testNext() {
        CustomPageRequest pageRequest = (CustomPageRequest) CustomPageRequest.create(10, 20);
        CustomPageRequest nextPageRequest = (CustomPageRequest) pageRequest.next();
        assertEquals(30, nextPageRequest.getOffset());
        assertEquals(20, nextPageRequest.getPageSize());
        assertEquals(Sort.unsorted(), nextPageRequest.getSort());
    }

    @Test
    void testPreviousOrFirst() {
        CustomPageRequest pageRequest = (CustomPageRequest) CustomPageRequest.create(10, 20);
        CustomPageRequest previousOrFirstPageRequest = (CustomPageRequest) pageRequest.previousOrFirst();
        assertEquals(10, previousOrFirstPageRequest.getOffset());
        assertEquals(20, previousOrFirstPageRequest.getPageSize());
        assertEquals(Sort.unsorted(), previousOrFirstPageRequest.getSort());
    }

    @Test
    void testFirst() {
        CustomPageRequest pageRequest = (CustomPageRequest) CustomPageRequest.create(10, 20);
        CustomPageRequest firstPageRequest = (CustomPageRequest) pageRequest.first();
        assertEquals(10, firstPageRequest.getOffset());
        assertEquals(20, firstPageRequest.getPageSize());
        assertEquals(Sort.unsorted(), firstPageRequest.getSort());
    }

    @Test
    void testWithPage() {
        CustomPageRequest pageRequest = (CustomPageRequest) CustomPageRequest.create(10, 20);
        CustomPageRequest withPageRequest = (CustomPageRequest) pageRequest.withPage(2);
        assertEquals(50, withPageRequest.getOffset());
        assertEquals(20, withPageRequest.getPageSize());
        assertEquals(Sort.unsorted(), withPageRequest.getSort());
    }

    @Test
    void testHasPrevious() {
        CustomPageRequest pageRequest = (CustomPageRequest) CustomPageRequest.create(10, 20);
        assertFalse(pageRequest.hasPrevious());
    }
}