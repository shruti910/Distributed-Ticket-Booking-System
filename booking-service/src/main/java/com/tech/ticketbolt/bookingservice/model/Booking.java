package com.tech.ticketbolt.bookingservice.model;

import com.tech.ticketbolt.bookingservice.util.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "BOOKING_ID", nullable = false, unique = true, length = 36) //UUID
    private String bookingId;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "QUANTITY", nullable = false)
    private Long quantity;

    @Column(name = "PLACED_AT", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime placedAt;

    @Column(name= "EVENT_ID", nullable = false)
    private Long eventId;

    @Enumerated(EnumType.STRING)
    @Column(name="STATUS", nullable = false)
    private BookingStatus status;

    @ManyToOne (fetch = FetchType.LAZY )
    @JoinColumn(name = "CUSTOMER_ID", nullable = true)
    private Customer customer;

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingId='" + bookingId + '\'' +
                ", totalPrice=" + totalPrice +
                ", quantity=" + quantity +
                ", placedAt=" + placedAt +
                ", eventId=" + eventId +
                ", status='" + status + '\'' +
                ", customer=" + customer +
                '}';
    }
}
