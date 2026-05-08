package com.tech.ticketbolt.inventoryservice.service;

import com.tech.ticketbolt.inventoryservice.enums.SeatAllocationStatus;
import com.tech.ticketbolt.inventoryservice.model.Event;
import com.tech.ticketbolt.inventoryservice.model.SeatAllocation;
import com.tech.ticketbolt.inventoryservice.repository.EventRepository;
import com.tech.ticketbolt.inventoryservice.repository.SeatAllocationRepository;
import com.tech.ticketbolt.orderservice.event.OrderEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;


@Service
public class KafkaListenerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaListenerService.class);
    private EventRepository eventRepository;
    private SeatAllocationRepository seatAllocationRepository;

    @Autowired
    public KafkaListenerService(EventRepository eventRepository, SeatAllocationRepository seatAllocationRepository){

        this.eventRepository = eventRepository;
        this.seatAllocationRepository = seatAllocationRepository;
    }

    @KafkaListener(topics = "order")
    @Transactional
    public void OrderEventListener(OrderEvent orderEvent) {
        logger.info("Order event received:{}", orderEvent);

        Optional<SeatAllocation> optionalSeatAllocation =  seatAllocationRepository.findByBookingId(orderEvent.bookingId());
        if(optionalSeatAllocation.isPresent()){
            SeatAllocation seatAllocation = optionalSeatAllocation.get();
            if ("CONFIRMED".equals(orderEvent.status())) {
                if(seatAllocation.getStatus() == SeatAllocationStatus.CONFIRMED){
                    logger.info("Duplicate Order Found in Database: {}", seatAllocation.toString());
                    logger.info("Skipping..");

                }else if(seatAllocation.getStatus() == SeatAllocationStatus.CANCELLED
                || seatAllocation.getStatus() == SeatAllocationStatus.EXPIRED) {
                    logger.info("CONFLICT Found in Database: {}", seatAllocation.toString());
                    logger.info("Skipping..");
                }else if(seatAllocation.getStatus() == SeatAllocationStatus.RESERVED) {
                    seatAllocation.setStatus(SeatAllocationStatus.CONFIRMED);
                    seatAllocationRepository.save(seatAllocation);
                }
            }

            else if("FAILED".equals(orderEvent.status())){
                if(seatAllocation.getStatus() == SeatAllocationStatus.CANCELLED){
                    logger.info("Duplicate Order Found in Database: {}", seatAllocation.toString());
                    logger.info("Skipping..");

                }else if(seatAllocation.getStatus() == SeatAllocationStatus.CONFIRMED
                        || seatAllocation.getStatus() == SeatAllocationStatus.EXPIRED) {
                    logger.info("CONFLICT Found in Database: {}", seatAllocation.toString());
                    logger.info("Skipping..");
                }else if(seatAllocation.getStatus() == SeatAllocationStatus.RESERVED){

                    int rows = eventRepository.addBackCapacity(orderEvent.eventId(), orderEvent.ticketCount());
                    if (rows > 0) {
                        seatAllocation.setStatus(SeatAllocationStatus.CANCELLED);
                        seatAllocationRepository.save(seatAllocation);
                        logger.info("Inventory returned for failed Order: {}", orderEvent.orderId());
                    }
                }
            }
        }else{
            logger.error("ERROR: Booking ID :{} not found", orderEvent.bookingId());
        }
    }
}

