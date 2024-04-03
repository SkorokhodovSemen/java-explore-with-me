package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.EventService;
import ru.practicum.events.dto.*;
import ru.practicum.request.EventRequestService;
import ru.practicum.request.dto.EventRequestDto;
import ru.practicum.request.dto.ListChangeStatusEventRequestDto;
import ru.practicum.request.dto.ListEventRequestIdDto;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final EventService eventService;
    private final EventRequestService eventRequestService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventCreatedDto createEvent(@Validated(Create.class) @RequestBody EventEntityDto eventEntityDto,
                                       @PathVariable("userId") long userId) {
        log.info("Create new event = {}, from user with id = {}", eventEntityDto, userId);
        return eventService.createEvent(eventEntityDto, userId);
    }

    @GetMapping("/{userId}/events")
    public List<EventDto> getEvents(@PathVariable("userId") long userId,
                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                    @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get events with userId = {}", userId);
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventById(@PathVariable("userId") long userId,
                                     @PathVariable("eventId") long eventId) {
        log.info("Get event with id = {} from user with id = {}", userId, eventId);
        return eventService.getEventById(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<EventRequestDto> getRequests(@PathVariable("userId") long userId,
                                             @PathVariable("eventId") long eventId) {
        log.info("Get requests for user id = {} for event id = {}", userId, eventId);
        return eventRequestService.getRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventAdminPatchDto updateEvent(@Validated(Update.class) @RequestBody EventAdminDto eventEntityDto,
                                          @PathVariable("userId") long userId,
                                          @PathVariable("eventId") long eventId) {
        log.info("Update event with id = {} from user id = {} \n" +
                "eventEntityDto = {}", eventId, userId, eventEntityDto);
        return eventService.updateEvent(eventEntityDto, userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ListChangeStatusEventRequestDto updateStatusEventRequest(@Validated(Update.class) @RequestBody ListEventRequestIdDto requestIdDto,
                                                                    @PathVariable("userId") long userId,
                                                                    @PathVariable("eventId") long eventId) {
        log.info("Update event with id = {} from user id = {}", eventId, userId);
        return eventRequestService.updateStatusEventRequest(requestIdDto, userId, eventId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public EventRequestDto createEventRequest(@RequestParam long eventId,
                                              @PathVariable("userId") long userId) {
        log.info("Create new EventRequest for Event with id = {} from user with id = {}", eventId, userId);
        return eventRequestService.createEventRequest(eventId, userId);
    }

    @GetMapping("/{userId}/requests")
    public List<EventRequestDto> getEventRequests(@PathVariable("userId") long userId) {
        log.info("Get EventRequest from user with id = {}", userId);
        return eventRequestService.getEventRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public EventRequestDto canceledEventRequest(@PathVariable("userId") long userId,
                                                @PathVariable("requestId") long requestId) {
        log.info("Canceled EventRequest with id = {} from user with id = {}", requestId, userId);
        return eventRequestService.canceledEventRequest(userId, requestId);
    }
}
