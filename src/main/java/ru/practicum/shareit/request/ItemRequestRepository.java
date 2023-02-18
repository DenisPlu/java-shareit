package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterId(Long requesterId);

    @Query(value = "SELECT * FROM ITEM_REQUESTS WHERE ((ID > ?) AND (REQUESTER_ID <> ?)) LIMIT ?", nativeQuery = true)
    List<ItemRequest> searchFromWithLimit(int from, Long ownerId, int size);
}
