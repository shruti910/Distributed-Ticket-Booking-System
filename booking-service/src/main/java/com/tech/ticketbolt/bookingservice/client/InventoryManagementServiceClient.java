package com.tech.ticketbolt.bookingservice.client;

import com.tech.ticketbolt.bookingservice.dto.InventoryBookingRequest;
import com.tech.ticketbolt.bookingservice.request.BookingRequest;
import com.tech.ticketbolt.bookingservice.response.EventResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InventoryManagementServiceClient {

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public InventoryManagementServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EventResponse reserveSeats(final @NonNull BookingRequest bookingRequest, final @NonNull String uniqueBookingId) {
        try {
            InventoryBookingRequest inventoryBookingRequest = new InventoryBookingRequest(uniqueBookingId,bookingRequest.userId(),
                    bookingRequest.eventId(), bookingRequest.ticketCount());

            return restTemplate.postForObject(inventoryServiceUrl, inventoryBookingRequest, EventResponse.class);

        } catch (ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Inventory Service is Unavailable");
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
