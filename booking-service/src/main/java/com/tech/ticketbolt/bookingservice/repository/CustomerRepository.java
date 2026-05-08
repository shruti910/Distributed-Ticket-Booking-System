package com.tech.ticketbolt.bookingservice.repository;

import com.tech.ticketbolt.bookingservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

}
