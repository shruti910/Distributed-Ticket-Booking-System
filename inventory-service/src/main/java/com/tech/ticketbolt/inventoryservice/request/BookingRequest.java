package com.tech.ticketbolt.inventoryservice.request;

public record BookingRequest(String bookingId,
                             Long userId,
                             Long eventId,
                             Long ticketCount) {
}
