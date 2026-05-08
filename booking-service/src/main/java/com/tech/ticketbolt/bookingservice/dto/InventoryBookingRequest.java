package com.tech.ticketbolt.bookingservice.dto;

public record InventoryBookingRequest(String bookingId,
                                      Long userId,
                                      Long eventId,
                                      Long ticketCount) {
}
