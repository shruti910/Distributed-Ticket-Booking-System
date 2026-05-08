package com.tech.ticketbolt.bookingservice.request;

public record BookingRequest(Long userId,
                             Long eventId,
                             Long ticketCount) {
}
