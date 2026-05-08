package com.tech.ticketbolt.inventoryservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name= "EVENT")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 255)
    private String name;
    @Column(name = "TOTAL_CAPACITY", nullable = true)
    private Long totalCapacity;
    @Column(name = "REMAINING_CAPACITY", nullable = true)
    private Long remainingCapacity;
    @Column(name = "EVENT_DATE", nullable = true)
    private LocalDateTime eventDate;
    @Column(name = "TICKET_PRICE", nullable = false)
    private BigDecimal ticketPrice;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "VENUE_ID", nullable = true)
    private Venue venue;

    public Event(Long id) {
        this.id = id;
    }


}
