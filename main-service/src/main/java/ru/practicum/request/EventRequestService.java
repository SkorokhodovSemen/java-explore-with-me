package ru.practicum.request;

import ru.practicum.request.dto.EventRequestDto;
import ru.practicum.request.dto.ListChangeStatusEventRequestDto;
import ru.practicum.request.dto.ListEventRequestIdDto;

import java.util.List;

public interface EventRequestService {
    List<EventRequestDto> getRequests(long userId, long eventId);
    ListChangeStatusEventRequestDto updateStatusEventRequest(ListEventRequestIdDto listEventRequestIdDto,
                                                                   long userId, long eventId);

    EventRequestDto createEventRequest(long eventId, long userId);

    List<EventRequestDto> getEventRequests(long userId);

    EventRequestDto canceledEventRequest(long userId, long requestId);
}
