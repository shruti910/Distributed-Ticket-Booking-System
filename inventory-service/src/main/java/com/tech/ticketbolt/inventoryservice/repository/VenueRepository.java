package com.tech.ticketbolt.inventoryservice.repository;

import com.tech.ticketbolt.inventoryservice.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Venue> getVenueById(Long venueId);
}
