package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterId(Long requesterId);

    @Query(value = "SELECT * FROM item_requests WHERE (requester_id <> ?) LIMIT ? OFFSET ?", nativeQuery = true)
    List<ItemRequest> searchFromWithLimit(Long ownerId, Integer size, Integer from);

    @Query(value = "SELECT * FROM item_requests WHERE (requester_id <> ?) OFFSET ?", nativeQuery = true)
    List<ItemRequest> searchFromWithoutLimit(Long ownerId, Integer from);
}
