package com.tech.ticketbolt.bookingservice.service;

import com.tech.ticketbolt.bookingservice.model.Booking;
import com.tech.ticketbolt.bookingservice.repository.BookingRepository;
import com.tech.ticketbolt.bookingservice.util.BookingStatus;
import com.tech.ticketbolt.orderservice.event.OrderEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@Transactional
public class KafkaListenerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaListenerService.class);
    private BookingRepository bookingRepository;
    @Autowired
    public KafkaListenerService(BookingRepository bookingRepository){
        this.bookingRepository = bookingRepository;
    }

    @KafkaListener(topics = "order")
    public void OrderEventListener(OrderEvent orderEvent) {
        logger.info("Event Received from Order Service:{}", orderEvent);
        bookingRepository.findByBookingId(orderEvent.bookingId()).ifPresentOrElse(booking -> {
            if (BookingStatus.PENDING.equals(booking.getStatus())) {
                if ("CONFIRMED".equals(orderEvent.status())) {
                    booking.setStatus(BookingStatus.CONFIRMED);
                    logger.info("BOOKING CONFIRMED for ID: {}", orderEvent.bookingId());
                } else if ("FAILED".equals(orderEvent.status())) {
                    booking.setStatus(BookingStatus.FAILED);
                    logger.info("BOOKING FAILED for ID: {}", orderEvent.bookingId());
                }
                bookingRepository.save(booking);
            }
        }, () -> logger.error("Booking not found for ID: {}", orderEvent.bookingId()));
    }

}
