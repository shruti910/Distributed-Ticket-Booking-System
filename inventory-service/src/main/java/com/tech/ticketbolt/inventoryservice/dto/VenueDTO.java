package com.tech.ticketbolt.inventoryservice.dto;

import com.tech.ticketbolt.inventoryservice.model.Venue;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record VenueDTO(Long id,
                       String name,
                       Long totalCapacity,
                       String address) {

    public VenueDTO {
            Objects.requireNonNull(id, "Venue ID must not be null");
    }

    public static VenueDTO fromEntity(@NonNull Venue venue){
        if (venue == null) return null;
        return new VenueDTO(venue.getId(),venue.getName(), venue.getTotalCapacity(), venue.getAddress());
    }

}
