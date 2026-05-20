package com.tech.ticketbolt.bookingservice.event;

import java.util.Objects;

public record InventoryReclaimEvent (String bookingId, Long eventId, Long quantity) {

    public InventoryReclaimEvent {
        Objects.requireNonNull(bookingId, "Booking ID must not be null");
        Objects.requireNonNull(quantity, "Quantity must not be null");
        Objects.requireNonNull(eventId, "Event ID must not be null");
    }

}