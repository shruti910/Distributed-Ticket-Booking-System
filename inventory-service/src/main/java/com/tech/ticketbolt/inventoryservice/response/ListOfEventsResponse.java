package com.tech.ticketbolt.inventoryservice.response;

import com.tech.ticketbolt.inventoryservice.dto.EventDTO;


import java.util.List;

public record ListOfEventsResponse(
    List<EventDTO> eventDTOList
){ }


