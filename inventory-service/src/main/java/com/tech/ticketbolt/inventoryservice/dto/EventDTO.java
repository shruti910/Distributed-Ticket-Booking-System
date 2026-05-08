package com.tech.ticketbolt.inventoryservice.dto;

import com.tech.ticketbolt.inventoryservice.model.Event;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record EventDTO(Long id,
                       String name,
                       Long totalCapacity,
                       Long remainingCapacity,
                       LocalDateTime eventDate,
                       BigDecimal ticketPrice,
                       VenueDTO venue) {

    public EventDTO {
        Objects.requireNonNull(id, "Event ID must not be null");
    }

    public static @NonNull EventDTO fromEntity(@NonNull Event event){
        VenueDTO venue = (event.getVenue()!=null) ? VenueDTO.fromEntity(event.getVenue()) : null;
        return new EventDTO(event.getId(),event.getName(), event.getTotalCapacity(),
                event.getRemainingCapacity(), event.getEventDate(), event.getTicketPrice(),venue);
    }


}
