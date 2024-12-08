package ru.practicum.request.storage;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;

import java.util.List;


public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestor(User requestor);

    List<ItemRequest> findByRequestorNot(User requestor, Pageable pageable);
}
