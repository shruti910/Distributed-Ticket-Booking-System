package com.tech.ticketbolt.bookingservice.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="CUSTOMER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = true, length = 255)
    private String name;
    @Column(name = "EMAIL", nullable = true, length = 255)
    private String email;
    @Column(name = "ADDRESS", nullable = true, length = 255)
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
