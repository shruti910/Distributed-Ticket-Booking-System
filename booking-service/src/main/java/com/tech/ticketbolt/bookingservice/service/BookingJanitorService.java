package com.tech.ticketbolt.bookingservice.service;

import com.tech.ticketbolt.bookingservice.event.InventoryReclaimEvent;
import com.tech.ticketbolt.bookingservice.model.Booking;
import com.tech.ticketbolt.bookingservice.repository.BookingRepository;
import com.tech.ticketbolt.bookingservice.util.BookingStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingJanitorService {

    private static final Logger logger = LoggerFactory.getLogger(BookingJanitorService.class);

    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    @Autowired
    public BookingJanitorService(BookingRepository bookingRepository, BookingService bookingService) {
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
    }

    @Scheduled(fixedRate = 60000)
    public void cleanExpiredPendingBookings() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(15);

        // Find bookings that NEVER made it to the Order phase
       List<Booking> expiredBookings = bookingRepository.findAllByStatusAndPlacedAtBefore(BookingStatus.PENDING, cutoffTime);

        if (expiredBookings.isEmpty()) {
            logger.debug("Janitor check completed: No expired pending bookings found.");
            return;
        }

        logger.info("Janitor running: Found {} expired pending bookings to reclaim.", expiredBookings.size());
            for (Booking booking : expiredBookings) {
                try {
                    bookingService.expireAndNotifyBooking(booking);
                } catch (Exception e) {
                    logger.error("Failed to expire booking {}. Skipping to next booking.", booking.getBookingId(), e);
                }
            }
    }
}
