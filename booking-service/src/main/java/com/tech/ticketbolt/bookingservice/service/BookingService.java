package com.tech.ticketbolt.bookingservice.service;


import com.tech.ticketbolt.bookingservice.client.InventoryManagementServiceClient;
import com.tech.ticketbolt.bookingservice.event.BookingEvent;
import com.tech.ticketbolt.bookingservice.event.InventoryReclaimEvent;
import com.tech.ticketbolt.bookingservice.model.Booking;
import com.tech.ticketbolt.bookingservice.model.Customer;
import com.tech.ticketbolt.bookingservice.repository.BookingRepository;
import com.tech.ticketbolt.bookingservice.repository.CustomerRepository;
import com.tech.ticketbolt.bookingservice.request.BookingRequest;
import com.tech.ticketbolt.bookingservice.response.BookingResponse;
import com.tech.ticketbolt.bookingservice.response.EventResponse;
import com.tech.ticketbolt.bookingservice.util.BookingStatus;
import com.tech.ticketbolt.bookingservice.util.EventResponseCode;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;


@Service
public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    private final BookingService self;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final InventoryManagementServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;
    private final KafkaTemplate<String, InventoryReclaimEvent> kafkaTemplateInventory;

    @Autowired
    public BookingService(@Lazy BookingService self, CustomerRepository customerRepository, BookingRepository bookingRepository, InventoryManagementServiceClient inventoryServiceClient, KafkaTemplate<String, BookingEvent> kafkaTemplate, KafkaTemplate<String, InventoryReclaimEvent> kafkaTemplateInventory) {
        this.self = self;
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateInventory = kafkaTemplateInventory;
    }


    public BookingResponse createBooking(@NonNull BookingRequest bookingRequest) {
        logger.debug("Booking Order Requested: {}", bookingRequest);
        // find if customer exists
        Customer customer = customerRepository.findById(bookingRequest.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        //find if Event and Venue exists, and reserve seats if there is enough inventory
        String uniqueBookingId = UUID.randomUUID().toString();
        EventResponse inventoryEventResponse = inventoryServiceClient.reserveSeats(bookingRequest, uniqueBookingId);
        logger.info("Response from Inventory Service:{}", inventoryEventResponse.toString());
        if (inventoryEventResponse.responseCode().equals(EventResponseCode.SUCCESS)) {
            return self.createPendingBookingReservation(bookingRequest, customer, inventoryEventResponse);
        }
        else if (inventoryEventResponse.responseCode().equals(EventResponseCode.SOLD_OUT)) {
            logger.info("Sorry, tickets sold out while you were looking!");
            return new BookingResponse(
                    uniqueBookingId,
                    customer.getId(),
                    inventoryEventResponse.eventId(),
                    inventoryEventResponse.venue().id(),
                    bookingRequest.ticketCount(),
                    null,
                    "Sorry, tickets sold out while you were looking!"
            );
        }
            return new BookingResponse(
                    uniqueBookingId,
                    customer.getId(),
                    inventoryEventResponse.eventId(),
                    inventoryEventResponse.venue().id(),
                    bookingRequest.ticketCount(),
                    null,
                    "ERROR! Sorry for the inconvenience"
            );
    }

    @Transactional
    public BookingResponse createPendingBookingReservation(@NonNull BookingRequest bookingRequest, Customer customer, EventResponse inventoryEventResponse) {

        //calculate total price of tickets
        BigDecimal totalPrice = BigDecimal.valueOf(bookingRequest.ticketCount())
                .multiply(inventoryEventResponse.ticketPrice());

        // 1. Prepare Entity
        Booking booking = new Booking();
        booking.setBookingId(inventoryEventResponse.bookingId());
        booking.setCustomer(customer);
        booking.setEventId(inventoryEventResponse.eventId());
        booking.setQuantity(bookingRequest.ticketCount());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        // 2. Save to DB
        bookingRepository.save(booking);

        // 3. Prepare Kafka Event
        BookingEvent bookingEvent = new BookingEvent(
                inventoryEventResponse.bookingId(),
                customer.getId(),
                inventoryEventResponse.eventId(),
                inventoryEventResponse.venue().id(),
                bookingRequest.ticketCount(),
                totalPrice,
                customer.getEmail()
        );

        // 4. Send to Kafka
        kafkaTemplate.send("booking", bookingEvent);

        // 5. Return Response
        return new BookingResponse(
                inventoryEventResponse.bookingId(),
                customer.getId(),
                inventoryEventResponse.eventId(),
                inventoryEventResponse.venue().id(),
                bookingRequest.ticketCount(),
                totalPrice,
                "Seats reserved..Order Pending..."
        );

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void expireAndNotifyBooking(Booking booking) {
        booking.setStatus(BookingStatus.EXPIRED);
        bookingRepository.save(booking);

        // Send event to Kafka so the Inventory Service can increment capacity
        InventoryReclaimEvent reclaimEvent = new InventoryReclaimEvent(
                booking.getBookingId(),
                booking.getEventId(),
                booking.getQuantity()
        );
        kafkaTemplateInventory.send("inventory-reclaim-topic", reclaimEvent);
        logger.info("Successfully expired booking {} and published Kafka reclaim event for event {}.",
                booking.getBookingId(), booking.getEventId());
    }

}


