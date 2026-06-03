package com.tech.ticketbolt.bookingservice.controller;

import com.tech.ticketbolt.bookingservice.request.BookingRequest;
import com.tech.ticketbolt.bookingservice.response.BookingResponse;
import com.tech.ticketbolt.bookingservice.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1/booking")
public class BookingController {
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    @Autowired
    public BookingController ( final BookingService bookingService){
        this.bookingService = bookingService;
    }

    @Operation(summary = "Create a Ticket Booking")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",  description = "SUCCESS"),
            @ApiResponse(responseCode= "400",  description = "Error: Invalid Credentials"),
            @ApiResponse(responseCode= "400",  description = "Error: Unauthorized"),
            @ApiResponse(responseCode= "403",  description = "Error: Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(value = "create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBooking (@RequestBody BookingRequest bookingRequest){
        try {
            BookingResponse response =  bookingService.createBooking(bookingRequest);
            return ResponseEntity.ok().body(response);
        }catch (ResourceAccessException e) {
            logger.debug(e.getMessage());
            return ResponseEntity.status(503).body(e.getMessage());
        }
        catch (RuntimeException e) {
            logger.debug(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }





}
