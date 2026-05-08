package com.tech.ticketbolt.orderservice.repository;

import com.tech.ticketbolt.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


     Optional<Order> findByBookingId(String bookingId);
}
