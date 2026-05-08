package com.tech.ticketbolt.inventoryservice.repository;


import com.tech.ticketbolt.inventoryservice.model.SeatAllocation;
import com.tech.ticketbolt.inventoryservice.service.KafkaListenerService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatAllocationRepository extends JpaRepository<SeatAllocation, Long> {


    Optional<SeatAllocation> findByBookingId(String bookingId);

}
