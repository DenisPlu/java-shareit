package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<BookingDto> getAllByUser(BookingRequestState state, Long userId, Integer from, String size) {
        if (Optional.ofNullable(userRepository.getReferenceById(userId).getName()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя, указанного в запросе не существует в базе");
        } else {
            List<Booking> bookingListResult;
            if (size.equals("NoLimit")) {
                bookingListResult = bookingRepository.findByBookerIdWithoutPagination(userId, from);
            } else if ((from < 0) || (Integer.parseInt(size) < 0) || ((from == 0) && (Integer.parseInt(size) == 0))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное значение параметров");
            } else {
                bookingListResult = bookingRepository.findByBookerIdWithPagination(userId, from, Integer.parseInt(size));
            }
            return filterBookingsByState(bookingListResult, state);
        }
    }

    private List<BookingDto> filterBookingsByState(List<Booking> bookings, BookingRequestState state) {
        List<BookingDto> bookingsDtoGetList = new ArrayList<>();
        switch (state) {
            case ALL:
                for (Booking booking: bookings) {
                    bookingsDtoGetList.add(BookingMapper.toBookingDto(booking,
                            UserMapper.toUserDtoId(userRepository.getReferenceById(booking.getBookerId())),
                            ItemMapper.toItemDtoMin(getItemFromBookingId(booking.getItemId()))));
                }
                break;
            case CURRENT:
                for (Booking booking: bookings) {
                    if (booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now())) {
                        bookingsDtoGetList.add(BookingMapper.toBookingDto(booking,
                                UserMapper.toUserDtoId(userRepository.getReferenceById(booking.getBookerId())),
                                ItemMapper.toItemDtoMin(getItemFromBookingId(booking.getItemId()))));
                    }
                }
                break;
            case PAST:
                for (Booking booking: bookings) {
                    if (booking.getEnd().isBefore(LocalDateTime.now())) {
                        bookingsDtoGetList.add(BookingMapper.toBookingDto(booking,
                                UserMapper.toUserDtoId(userRepository.getReferenceById(booking.getBookerId())),
                                ItemMapper.toItemDtoMin(getItemFromBookingId(booking.getItemId()))));
                    }
                }
                break;
            case FUTURE:
                for (Booking booking: bookings) {
                    if (booking.getStart().isAfter(LocalDateTime.now())) {
                        bookingsDtoGetList.add(BookingMapper.toBookingDto(booking,
                                UserMapper.toUserDtoId(userRepository.getReferenceById(booking.getBookerId())),
                                ItemMapper.toItemDtoMin(getItemFromBookingId(booking.getItemId()))));
                    }
                }
                break;
            case WAITING:
                for (Booking booking: bookings) {
                    if (booking.getStatus().equals(BookingStatus.WAITING)) {
                        bookingsDtoGetList.add(BookingMapper.toBookingDto(booking,
                                UserMapper.toUserDtoId(userRepository.getReferenceById(booking.getBookerId())),
                                ItemMapper.toItemDtoMin(getItemFromBookingId(booking.getItemId()))));
                    }
                }
            case REJECTED:
                for (Booking booking: bookings) {
                    if (booking.getStatus().equals(BookingStatus.REJECTED)) {
                        bookingsDtoGetList.add(BookingMapper.toBookingDto(booking,
                                UserMapper.toUserDtoId(userRepository.getReferenceById(booking.getBookerId())),
                                ItemMapper.toItemDtoMin(getItemFromBookingId(booking.getItemId()))));
                    }
                }
                break;
        }
        bookingsDtoGetList.sort(Comparator.comparing(BookingDto::getStart).reversed());
        return bookingsDtoGetList;
    }

    @Override
    public List<BookingDto> getAllByOwner(BookingRequestState state, Long userId, Integer from, String size) {
        if (Optional.ofNullable(userRepository.getReferenceById(userId).getName()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя, указанного в запросе не существует в базе");
        } else {
            List<Item> itemListResult = itemRepository.findByOwner(userId);
            List<Booking> bookingListResult = new ArrayList<>();
            if (size.equals("NoLimit")) {
                for (Item item : itemListResult) {
                    bookingListResult.addAll(bookingRepository.findByItemIdWithoutPagination(item.getId(), from));
                }
            } else if ((from < 0) || (Integer.parseInt(size) < 0) || ((from == 0) && (Integer.parseInt(size) == 0))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное значение параметров");
            } else {
                for (Item item : itemListResult) {
                    bookingListResult.addAll(bookingRepository.findByItemIdWithPagination(item.getId(), from, Integer.parseInt(size)));
                }
            }
            return filterBookingsByState(bookingListResult, state);
        }
    }

    @Override
    public BookingDto get(Long id, Long userId) {
        if (Optional.ofNullable(bookingRepository.getReferenceById(id)).isPresent()) {
            if (bookingRepository.getReferenceById(id).getBookerId().equals(userId)
                    || itemRepository.getReferenceById(bookingRepository.getReferenceById(id).getItemId()).getOwner().equals(userId)) {
                return BookingMapper.toBookingDto(bookingRepository.getReferenceById(id),
                        UserMapper.toUserDtoId(userRepository.getReferenceById(bookingRepository.getReferenceById(id).getBookerId())),
                        ItemMapper.toItemDtoMin(getItemFromBookingId(bookingRepository.getReferenceById(id).getItemId())));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не корректно задан id пользователя");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не корректно задан id");
        }
    }

    @Override
    public BookingDto create(Booking booking, Long bookerId) {
        try {
            if (itemRepository.getReferenceById(booking.getItemId()).getOwner().equals(bookerId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нельзя забронировать собственную вещь");
            } else {
                if (itemRepository.getReferenceById(booking.getItemId()).getAvailable().equals("true")) {
                    if (!userRepository.getReferenceById(bookerId).equals(null)) {
                        if (booking.getEnd().isBefore(booking.getStart())) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата окончания бронирования указана до начала брорирования");
                        } else {
                            if (booking.getStart().isBefore(LocalDateTime.now())) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата начала бронирования должна быть указана в будущем");
                            } else {
                                booking.setBookerId(bookerId);
                                booking.setStatus(BookingStatus.WAITING);
                                return BookingMapper.toBookingDto(bookingRepository.save(booking),
                                        UserMapper.toUserDtoId(userRepository.getReferenceById(bookerId)),
                                        ItemMapper.toItemDtoMin(getItemFromBookingId(booking.getItemId())));
                            }
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User с заданным id в заголовке X-Sharer-User-Id не существует");
                    }
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не задана Available - доступность item");
                }
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не корректно задан item");
        }
    }

    @Override
    public BookingDto update(Long id, Long ownerId, String approved) {
        Booking updatedBooking = bookingRepository.getReferenceById(id);
        if (itemRepository.getReferenceById(updatedBooking.getItemId()).getOwner().equals(ownerId)) {
            if (updatedBooking.getStatus().equals(BookingStatus.APPROVED) && approved.equals("true")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректно задан статус, бронирование уже подтверждено");
            } else {
                if (Boolean.parseBoolean(approved)) {
                    updatedBooking.setStatus(BookingStatus.APPROVED);
                } else {
                    updatedBooking.setStatus(BookingStatus.REJECTED);
                }
                return BookingMapper.toBookingDto(bookingRepository.save(updatedBooking),
                        UserMapper.toUserDtoId(userRepository.getReferenceById(updatedBooking.getBookerId())),
                        ItemMapper.toItemDtoMin(getItemFromBookingId(updatedBooking.getItemId())));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректно задан статус или владелец");
        }
    }

    @Override
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }

    private Item getItemFromBookingId(Long itemId) {
        return itemRepository.getReferenceById(itemId);
    }
}
