package com.tech.ticketbolt.orderservice.service;


import com.tech.ticketbolt.orderservice.event.OrderEvent;
import com.tech.ticketbolt.orderservice.model.Order;
import com.tech.ticketbolt.orderservice.repository.OrderRepository;
import com.tech.ticketbolt.orderservice.util.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderJanitorService {

    private static final Logger logger = LoggerFactory.getLogger(OrderJanitorService.class);

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    @Autowired
    public OrderJanitorService(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }


    @Scheduled(fixedRate = 60000)
    public void cleanUnpaidOrders() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(15);

        // Fetch orders stuck in PROCESSING state older than 15 mins
        List<Order> unpaidOrders = orderRepository.findAllByStatusAndCreatedAtBefore(OrderStatus.PROCESSING, cutoffTime);

        if (unpaidOrders.isEmpty()) {
            logger.debug("Janitor check completed: No pending orders found.");
            return;
        }

        logger.info("Order Janitor found {} unpaid orders to cancel.", unpaidOrders.size());
        unpaidOrders.forEach(order -> {

            try {
                orderService.cancelAndNotify(order);

            } catch (Exception e) {
                logger.error("Failed to cancel order {}. Skipping to next order.", order.getOrderId(), e);
            }
        });
    }
}
