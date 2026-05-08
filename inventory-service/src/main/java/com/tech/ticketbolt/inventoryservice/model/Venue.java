package com.tech.ticketbolt.inventoryservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VENUE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;
    @Column(name = "NAME", length = 255, nullable = true)
    private String name;
    @Column(name = "TOTAL_CAPACITY", nullable = true)
    private Long totalCapacity;
    @Column(name = "ADDRESS", length = 255, nullable = true)
    private String address;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Event> events;

    public Venue(Long id) {
        this.id = id;
    }

}
