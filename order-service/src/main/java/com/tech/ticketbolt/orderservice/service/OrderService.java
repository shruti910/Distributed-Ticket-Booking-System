package com.tech.ticketbolt.orderservice.service;

import com.tech.ticketbolt.bookingservice.event.BookingEvent;
import com.tech.ticketbolt.orderservice.event.OrderEvent;
import com.tech.ticketbolt.orderservice.repository.OrderRepository;
import com.tech.ticketbolt.orderservice.model.Order;
import com.tech.ticketbolt.orderservice.util.OrderStatus;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${order.payment.status}")
    String orderPaymentStatus;

    @KafkaListener(topics = "booking")
    public void createOrder(BookingEvent bookingEvent) {
        logger.info("Booking Event Received:{}", bookingEvent);
        //check if bookingId already exists in DB (duplicate request)
        orderRepository.findByBookingId(bookingEvent.bookingId()).ifPresentOrElse( order ->{
                    logger.info("Duplicate Request exists for booking ID:{}", bookingEvent.bookingId());
                    logger.info("Resending Event...");
                    sendOrderEvent(order);
        }, () -> {
            //Create Order
            Order order = mapToOrder(bookingEvent);
            //simulate Payment
            order = processPayment(order);
            //Persist in Orders DB
            Order finalOrder = orderRepository.save(order);
            logger.info("ORDER CREATED: {}", finalOrder.toString());
            sendOrderEvent(finalOrder);
                } );

        //send confirmation email to user (Not in scope of this project)
    }

        private void sendOrderEvent(Order finalOrder){

        //Send message to Booking and Inventory Services
        OrderEvent orderEvent = new OrderEvent( finalOrder.getOrderId(),
                finalOrder.getBookingId(),
                finalOrder.getCustomerId(),
                finalOrder.getEventId(),
                finalOrder.getVenueId(),
                finalOrder.getQuantity(),
                finalOrder.getTotalPrice(),
                finalOrder.getCustomerEmail(),
                finalOrder.getStatus().toString()
        );

        kafkaTemplate.send("order", orderEvent);
        logger.info("Kafka Event Sent for Order: {} with status: {}", finalOrder.getOrderId(), finalOrder.getStatus());
    }


    private Order mapToOrder(BookingEvent bookingEvent) {

        String orderId = UUID.randomUUID().toString();
        Order order = new Order();
        order.setBookingId(bookingEvent.bookingId());
        order.setOrderId(orderId);
        order.setEventId(bookingEvent.eventId());
        order.setVenueId(bookingEvent.venueId());
        order.setCustomerEmail(bookingEvent.email());
        order.setCustomerId(bookingEvent.customerId());
        order.setQuantity(bookingEvent.ticketCount());
        order.setTotalPrice(bookingEvent.totalPrice());
        order.setStatus(OrderStatus.PROCESSING);
        return order;

    }

    private Order processPayment(Order order) {
        order.setStatus(OrderStatus.valueOf(orderPaymentStatus.toUpperCase())); //CONFIRMED OR FAILED
        return order;
    }


}
