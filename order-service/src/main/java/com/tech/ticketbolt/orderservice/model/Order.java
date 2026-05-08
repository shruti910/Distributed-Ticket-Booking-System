package com.tech.ticketbolt.orderservice.model;


import com.tech.ticketbolt.orderservice.util.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "ORDER_ID", nullable = false, unique = true, length=36) //UUID
    private String orderId;
    @Column(name = "BOOKING_ID", nullable = false, unique = true, length = 36) //UUID
    private String bookingId;
    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;
    @Column(name = "CUSTOMER_EMAIL", nullable = false)
    private String customerEmail;
    @Column(name = "EVENT_ID", nullable = false)
    private Long eventId;
    @Column(name = "VENUE_ID", nullable = false)
    private Long venueId;
    @Column(name = "QUANTITY", nullable = false)
    private Long quantity;
    @Column(name = "TOTAL_PRICE", nullable = false)
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private OrderStatus status;
    @Column(name = "CREATED_AT", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
