package com.tech.ticketbolt.inventoryservice.model;

import com.tech.ticketbolt.inventoryservice.enums.SeatAllocationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "SEAT_ALLOCATIONS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeatAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EVENT_ID")
    private Long eventId;

    @Column(name = "BOOKING_ID", unique = true, nullable = false, length = 36)
    private String bookingId;

    @Column(name = "QUANTITY",nullable = false)
    private Long quantity;

    @Enumerated(EnumType.STRING)
    @Column(name ="STATUS", nullable = false)
    private SeatAllocationStatus status;

    @Column(name = "CREATED_AT", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

}
