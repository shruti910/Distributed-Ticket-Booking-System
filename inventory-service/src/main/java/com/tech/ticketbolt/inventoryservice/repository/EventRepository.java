package com.tech.ticketbolt.inventoryservice.repository;

import com.tech.ticketbolt.inventoryservice.model.Event;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {


    Optional<Event> getEventById(Long id);

    Optional<Event> findById(Long aLong);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.remainingCapacity = e.remainingCapacity - :qty " +
            "WHERE e.id = :id AND e.remainingCapacity >= :qty")
    int deductInventory(Long id, Long qty);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.remainingCapacity = e.remainingCapacity + :qty " +
            "WHERE e.id = :id AND (e.remainingCapacity + :qty) <= e.totalCapacity")
    int addBackCapacity(Long id, Long qty);
}
