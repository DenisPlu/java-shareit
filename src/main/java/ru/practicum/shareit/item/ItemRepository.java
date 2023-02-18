package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwner(Long ownerId);

    List<Item> findByRequestId(Long requestId);

    @Query(value = "SELECT * FROM ITEMS WHERE (UPPER(name) LIKE UPPER(CONCAT('%', ?1, '%')) OR UPPER(description) LIKE UPPER(CONCAT('%', ?1, '%'))) AND (available) LIKE 'true'", nativeQuery = true)
    List<Item> searchInNameAndDescription(String text);
}