package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoMin;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForUpdate;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    //реализовать поиск
    @Override
    public List<ItemDtoWithBooking> getAllByOwner(Long ownerId) {
        List<Item> itemListResult = itemRepository.findByOwner(ownerId);
        List<ItemDtoWithBooking> itemDtoGetAllList = new ArrayList<>();
        for (Item item: itemListResult) {
            BookingDtoMin lastBooking = findLastBooking(item.getId());
            BookingDtoMin nextBooking = findNextBooking(item.getId());
            List<CommentDto> commentsList = new ArrayList<>();
            for (Comment comment: commentRepository.findByItemId(ownerId)) {
                commentsList.add(CommentMapper.toCommentDto(comment, userRepository.getReferenceById(comment.getAuthorId()).getName()));
            }
            itemDtoGetAllList.add(ItemMapper.toItemDtoWithBooking(item, lastBooking, nextBooking, commentsList));
        }
        return itemDtoGetAllList;
    }

    @Override
    public ItemDtoWithBooking get(Long id, Long userId) {
        BookingDtoMin lastBooking = null;
        BookingDtoMin nextBooking = null;
        if (userId.equals(itemRepository.getReferenceById(id).getOwner())) {
            lastBooking = findLastBooking(id);
            nextBooking = findNextBooking(id);
        }
        List<CommentDto> commentsList = new ArrayList<>();
        for (Comment comment: commentRepository.findByItemId(id)) {
            commentsList.add(CommentMapper.toCommentDto(comment, userRepository.getReferenceById(comment.getAuthorId()).getName()));
        }
        return ItemMapper.toItemDtoWithBooking(itemRepository.getReferenceById(id), lastBooking, nextBooking, commentsList);
    }

    private BookingDtoMin findLastBooking(Long id) {
        List<Booking> listOfBookingsByItem = bookingRepository.findByItemId(id);
        if (listOfBookingsByItem.isEmpty()) {
            return null;
        } else {
            List<Booking> listOfBookingsEndsBeforeNow = listOfBookingsByItem.stream().filter(p1 -> p1.getEnd().isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getEnd).reversed()).collect(Collectors.toList());
            if (!listOfBookingsEndsBeforeNow.isEmpty()) {
                return BookingMapper.toBookingDtoMin(listOfBookingsEndsBeforeNow.stream().findFirst().get());
            } else {
                return null;
            }
        }
    }

    private BookingDtoMin findNextBooking(Long id) {
        List<Booking> listOfBookingsByItem = bookingRepository.findByItemId(id);
        if (listOfBookingsByItem.isEmpty()) {
            return null;
        } else {
            List<Booking> listOfBookingsStartsAfterNow = listOfBookingsByItem.stream().filter(p1 -> p1.getStart().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getStart)).collect(Collectors.toList());
            if (listOfBookingsStartsAfterNow.isEmpty()){
                return  null;
            } else {
                return BookingMapper.toBookingDtoMin(listOfBookingsStartsAfterNow.stream().findFirst().get());
            }
        }
    }

    @Override
    public List<ItemDto> searchInNameAndDescription(String text) {
        List<ItemDto> itemDtoGetAllList = new ArrayList<>();
        if (!text.isEmpty()) {
            List<Item> itemListResult = itemRepository.searchInNameAndDescription(text);
            for (Item item: itemListResult) {
                itemDtoGetAllList.add(ItemMapper.toItemDto(item));
            }
        }
        return itemDtoGetAllList;
    }

    @Override
    public ItemDto create(ItemDto item, Long ownerId) {
        if (item.isAvailable() && Optional.ofNullable(item.getDescription()).isPresent()) {
            if (!userRepository.getReferenceById(ownerId).equals(null) && item.isAvailable()) {
                ItemDto result = ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItemFromDto(item, ownerId)));
                return result;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User с заданным id в заголовке X-Sharer-User-Id не существует");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не задана Available - доступность или описание item");
        }
    }

    @Override
    public ItemDto update(Long id, ItemDtoForUpdate itemDto, Long ownerId) {
        Item updatedItem = itemRepository.getReferenceById(id);
        if (Optional.ofNullable(userRepository.getReferenceById(ownerId)).isPresent() && itemRepository.getReferenceById(id).getOwner().equals(ownerId)) {
            if (Optional.ofNullable(itemDto.getName()).isPresent()) {
                updatedItem.setName(itemDto.getName());
            }
            if (Optional.ofNullable(itemDto.getDescription()).isPresent()) {
                updatedItem.setDescription(itemDto.getDescription());
            }
            if (Optional.ofNullable(itemDto.getAvailable()).isPresent()) {
                if (itemDto.getAvailable().equals("true")) {
                    updatedItem.setAvailable("true");
                } else {
                    updatedItem.setAvailable("false");
                }
            }
            return ItemMapper.toItemDto(itemRepository.save(updatedItem));
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Некорректный id владельца");
        }
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public CommentDto createComment(Long itemId, Long authorId, Comment comment) {
        List<Booking> bookings  = bookingRepository.findByItemId(itemId).stream().filter(p1 -> p1.getBookerId().equals(authorId))
                .filter(p1 -> p1.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());
        if (bookings.isEmpty() || comment.getText().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "У пользователя не было бронирований данной вещи");
        } else {
            comment.setItemId(itemId);
            comment.setAuthorId(authorId);
            comment.setCreatedDate(LocalDateTime.now());
            return CommentMapper.toCommentDto(commentRepository.save(comment), userRepository.getReferenceById(authorId).getName());
        }
    }
}
