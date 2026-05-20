package com.tech.ticketbolt.orderservice.repository;

import com.tech.ticketbolt.orderservice.model.Order;
import com.tech.ticketbolt.orderservice.util.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


     Optional<Order> findByBookingId(String bookingId);

    List<Order> findAllByStatusAndCreatedAtBefore(OrderStatus orderStatus, LocalDateTime cutoffTime);
}
