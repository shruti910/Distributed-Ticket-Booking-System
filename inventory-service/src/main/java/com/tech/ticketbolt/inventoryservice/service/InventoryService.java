package com.tech.ticketbolt.inventoryservice.service;


import com.tech.ticketbolt.inventoryservice.enums.EventResponseCode;
import com.tech.ticketbolt.inventoryservice.enums.SeatAllocationStatus;
import com.tech.ticketbolt.inventoryservice.model.Event;
import com.tech.ticketbolt.inventoryservice.model.SeatAllocation;
import com.tech.ticketbolt.inventoryservice.model.Venue;
import com.tech.ticketbolt.inventoryservice.repository.EventRepository;
import com.tech.ticketbolt.inventoryservice.repository.SeatAllocationRepository;
import com.tech.ticketbolt.inventoryservice.repository.VenueRepository;
import com.tech.ticketbolt.inventoryservice.request.BookingRequest;
import com.tech.ticketbolt.inventoryservice.response.EventResponse;

import com.tech.ticketbolt.inventoryservice.response.VenueResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private final EventRepository eventRepository;
    private final VenueRepository  venueRepository;
    private final SeatAllocationRepository seatAllocationRepository;

    @Autowired
    public InventoryService(final EventRepository eventRepository, final VenueRepository venueRepository, SeatAllocationRepository seatAllocationRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.seatAllocationRepository = seatAllocationRepository;
    }




    public EventResponse reserveSeats(@NonNull BookingRequest bookingRequest) {

        Optional<Event> eventOpt = eventRepository.findById(bookingRequest.eventId());
        if (eventOpt.isEmpty()) {
            logger.error("Event not found: {}", bookingRequest.eventId());
            return new EventResponse(bookingRequest.eventId(), bookingRequest.bookingId(), null, bookingRequest.ticketCount(),
                    null, null, null,null,
                    "Event Not Found", EventResponseCode.EVENT_NOT_FOUND);
        }

        Event event = eventOpt.get();
        Venue venue = event.getVenue();
        Optional<Venue> venueOpt = venueRepository.findById(venue.getId());
        if (venueOpt.isEmpty()) {
            logger.error("Venue not found: {}", venue.getId());
            return new EventResponse(bookingRequest.eventId(), bookingRequest.bookingId(), null, bookingRequest.ticketCount(),
                    null, null, null,null,
                    "Venue Not Found", EventResponseCode.VENUE_NOT_FOUND);
        }

        VenueResponse venueResponse = new VenueResponse(
                venue.getId(), venue.getName(), venue.getTotalCapacity(), venue.getAddress(), null
        );
        //update EVENT table
        int rowsAffected = eventRepository.deductInventory(bookingRequest.eventId(), bookingRequest.ticketCount());

        if (rowsAffected > 0) {
            logger.debug("Inventory deducted, seats reserved successfully.");
            //update SEAT_ALLOCATIONS table status to RESERVED
            SeatAllocation allocation = new SeatAllocation();
            allocation.setEventId(bookingRequest.eventId());
            allocation.setBookingId(bookingRequest.bookingId());
            allocation.setQuantity(bookingRequest.ticketCount());
            allocation.setStatus(SeatAllocationStatus.RESERVED);
            seatAllocationRepository.save(allocation);
            logger.info("Inventory allocation recorded for Booking ID: {}", bookingRequest.bookingId());
            return new EventResponse(
                    event.getId(), bookingRequest.bookingId(),event.getName(), event.getTotalCapacity(),
                    event.getRemainingCapacity() - bookingRequest.ticketCount(), // Reflect the deduction
                    event.getEventDate(), event.getTicketPrice(), venueResponse,
                    "Seats reserved successfully", EventResponseCode.SUCCESS
            );
        } else {
            logger.debug("Not enough Inventory for eventId: {}", bookingRequest.eventId());
            return new EventResponse(
                    event.getId(), bookingRequest.bookingId(),event.getName(), event.getTotalCapacity(),
                    event.getRemainingCapacity(), event.getEventDate(), event.getTicketPrice(),
                    venueResponse, "Not Enough Inventory", EventResponseCode.SOLD_OUT
            );
        }
    }
}
