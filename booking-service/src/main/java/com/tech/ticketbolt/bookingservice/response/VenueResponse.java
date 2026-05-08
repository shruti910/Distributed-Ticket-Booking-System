package com.tech.ticketbolt.bookingservice.response;


public record VenueResponse(
    Long id,
    String name,
    Long totalCapacity,
    String address,
    String message
    ) { }
