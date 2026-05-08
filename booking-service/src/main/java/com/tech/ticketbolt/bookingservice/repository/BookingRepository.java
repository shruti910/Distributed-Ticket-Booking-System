package com.tech.ticketbolt.bookingservice.repository;

import com.tech.ticketbolt.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    Optional<Booking> findByBookingId(String bookingId);


}
