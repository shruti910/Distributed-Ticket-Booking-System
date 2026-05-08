package com.tech.ticketbolt.bookingservice.event;

import java.math.BigDecimal;
import java.util.Objects;

public record BookingEvent(
        String bookingId,
        Long customerId,
        Long eventId,
        Long venueId,
        Long ticketCount,
        BigDecimal totalPrice,
        String email
        ) {

    public BookingEvent {
        Objects.requireNonNull(bookingId, "Booking ID must not be null");
        Objects.requireNonNull(customerId, "Customer ID must not be null");
        Objects.requireNonNull(eventId, "Event ID must not be null");
    }
}
