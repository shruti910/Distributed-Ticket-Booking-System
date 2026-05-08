package com.tech.ticketbolt.bookingservice.response;


import com.tech.ticketbolt.bookingservice.util.EventResponseCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventResponse(
        Long eventId,
        String bookingId,
        String name,
        Long totalCapacity,
        Long remainingCapacity,
        LocalDateTime eventDate,
        BigDecimal ticketPrice,
        VenueResponse venue,
        String message,
        EventResponseCode responseCode
) { }
