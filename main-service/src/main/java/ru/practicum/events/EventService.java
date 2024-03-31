package ru.practicum.events;

import ru.practicum.events.dto.*;
import ru.practicum.events.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventCreatedDto createEvent(EventEntityDto eventEntityDto, long userId);

    List<EventDto> getEvents(long userId, int from, int size);

    EventFullDto getEventById(long userId, long eventId);

    EventAdminPatchDto updateEvent(EventAdminDto eventEntityDto, long userId, long eventId);

    List<EventAdminSearchDto> getEventsForAdmin(List<Long> users,
                                                List<State> states,
                                                List<Long> categories,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                int from,
                                                int size);

    EventAdminPatchDto updateEventByIdForAdmin(EventAdminDto eventAdminDto, long eventId);

    EventSearchDto getEventsByIdForQuery(long id, String uri);

    List<EventAdminSearchDto> getEventsForQuery(String text,
                                                Boolean paid,
                                                boolean onlyAvailable,
                                                List<Long> categories,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                String sort,
                                                int from,
                                                int size,
                                                String uri,
                                                String ip);
}
