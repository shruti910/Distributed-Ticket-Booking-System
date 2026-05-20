package com.tech.ticketbolt.inventoryservice.controller;

import com.tech.ticketbolt.inventoryservice.request.BookingRequest;
import com.tech.ticketbolt.inventoryservice.response.EventResponse;
import com.tech.ticketbolt.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/v1/inventory")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(final InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }




    @Operation(summary = "Reserve Seats for Booking Request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS"),
            @ApiResponse(responseCode = "400", description = "Error: Invalid Credentials"),
            @ApiResponse(responseCode = "400", description = "Error: Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Error: Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(value = "/reserve", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reserveSeats(@RequestBody final @NonNull BookingRequest bookingRequest) {
        try {
            logger.debug("Booking Request received:{}", bookingRequest);
            EventResponse response = inventoryService.reserveSeats(bookingRequest);
            return ResponseEntity.ok().body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad Request: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
