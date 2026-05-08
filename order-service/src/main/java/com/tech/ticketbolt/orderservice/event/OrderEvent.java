package com.tech.ticketbolt.orderservice.event;

import java.math.BigDecimal;

public record OrderEvent(
        String orderId,
        String bookingId,
        Long customerId,
        Long eventId,
        Long venueId,
        Long ticketCount,
        BigDecimal totalPrice,
        String email,
        String status) {
}
