package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwner(Long ownerId);

    @Query(value = "SELECT * FROM ITEMS WHERE (upper(name) like upper(concat('%', ?1, '%')) or upper(description) like upper(concat('%', ?1, '%'))) and (available) like 'true'", nativeQuery = true)
    List<Item> searchInNameAndDescription(String text);
}