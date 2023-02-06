package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @FutureOrPresent
    @Column(name = "start_date")
    LocalDateTime start;
    @Column(name = "end_date")
    @FutureOrPresent
    LocalDateTime end;
    Long bookerId;
    Long itemId;
    @Enumerated(EnumType.STRING)
    BookingStatus status;
}
