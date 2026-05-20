package com.tech.ticketbolt.bookingservice.repository;

import com.tech.ticketbolt.bookingservice.model.Booking;
import com.tech.ticketbolt.bookingservice.util.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    Optional<Booking> findByBookingId(String bookingId);

    List<Booking> findAllByStatusAndPlacedAtBefore(BookingStatus bookingStatus, LocalDateTime cutoffTime);
}
