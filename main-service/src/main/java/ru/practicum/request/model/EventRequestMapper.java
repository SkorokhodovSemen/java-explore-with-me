package ru.practicum.request.model;

import ru.practicum.request.dto.EventRequestDto;

public abstract class EventRequestMapper {
    public static EventRequest toEventRequest(EventRequestDto eventRequestDto) {
        EventRequest eventRequest = new EventRequest();
        return eventRequest;
    }

    public static EventRequestDto toEventRequestDto(EventRequest eventRequest) {
        EventRequestDto eventRequestDto = new EventRequestDto();
        eventRequestDto.setRequester(eventRequest.getRequester().getId());
        eventRequestDto.setId(eventRequest.getId());
        eventRequestDto.setCreated(eventRequest.getCreated());
        eventRequestDto.setStatus(eventRequest.getStatus());
        eventRequestDto.setEvent(eventRequest.getEvent().getId());
        return eventRequestDto;
    }
}
