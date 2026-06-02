package com.tech.ticketbolt.bookingservice.response;
import java.math.BigDecimal;
import java.util.Objects;

public record BookingResponse(
        String bookingId,
        Long customerId,
        Long eventId,
        Long venueId,
        Long ticketCount,
        BigDecimal totalPrice,
        String message
        ) {

    public BookingResponse {
        Objects.requireNonNull(bookingId, "Booking ID must not be null");
    }


}
