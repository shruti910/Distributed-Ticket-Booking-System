package com.tech.ticketbolt.inventoryservice.response;


public record VenueResponse (
    Long id,
    String name,
    Long totalCapacity,
    String address,
    String message
    ) { }
