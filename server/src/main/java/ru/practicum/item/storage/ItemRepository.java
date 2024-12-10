package ru.practicum.item.storage;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwner(User owner, Pageable pageable);

    @Query("select i.owner from Item i where i.id = ?1")
    User getItemOwner(Long itemId);

    @Query("select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            "   or upper(i.description) like upper(concat('%', ?1, '%'))")
    List<Item> search(@Param("text") String text, Pageable pageable);

    List<Item> findByRequest(ItemRequest itemRequest);

    @Query("select i from Item i where i.request.id in :requestIds")
    List<Item> findByRequestIds(@Param("requestIds") List<Long> requestIds);  //добавила

}