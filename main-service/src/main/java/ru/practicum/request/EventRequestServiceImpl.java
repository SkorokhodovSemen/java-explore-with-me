package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.EventRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.EventRequestDto;
import ru.practicum.request.dto.ListChangeStatusEventRequestDto;
import ru.practicum.request.dto.ListEventRequestIdDto;
import ru.practicum.request.model.EventRequest;
import ru.practicum.request.model.EventRequestMapper;
import ru.practicum.request.model.Status;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventRequestServiceImpl implements EventRequestService {
    private final EventRequestRepository eventRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<EventRequestDto> getRequests(long userId, long eventId) {
        Optional<User> userOptional = userRepository.findById(userId);
        validFoundForUser(userOptional, userId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        validFoundForEvent(eventOptional, eventId);
        return eventRequestRepository.getEventRequestsByEventId(eventId, userId)
                .stream()
                .map(EventRequestMapper::toEventRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ListChangeStatusEventRequestDto updateStatusEventRequest(ListEventRequestIdDto listEventRequestIdDto,
                                                                    long userId, long eventId) {
        Optional<User> userOptional = userRepository.findById(userId);
        validFoundForUser(userOptional, userId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        validFoundForEvent(eventOptional, eventId);
        Event event = eventOptional.get();
        if (userId != event.getInitiator().getId()) {
            log.info("User with id = {} can't change eventRequest for event with id = {} " +
                    "because it's not his Event", userId, eventId);
            throw new ConflictException("You can't change eventRequest for this Event");
        }
        List<EventRequest> eventRequests = eventRequestRepository
                .getEventRequestsByEventsId(listEventRequestIdDto.getRequestIds());
        if (eventRequests.size() == 1 && eventRequests.get(0).getStatus().equals(Status.CONFIRMED)) {
            throw new ConflictException("You can't rejected this request, because it's confirmed");
        }
        ListChangeStatusEventRequestDto listChangeStatusEventRequestDto = new ListChangeStatusEventRequestDto();
        List<EventRequestDto> eventRequestsConfirmed = new ArrayList<>();
        List<EventRequestDto> eventRequestsRejected = new ArrayList<>();
        long countEventRequestsConfirmed = eventRequestRepository.getCountConfirmedRequest(eventId);
        int i = 0;
        if (listEventRequestIdDto.getStatus().equals(Status.REJECTED.toString())) {
            for (EventRequest eventRequest : eventRequests) {
                eventRequest.setStatus(Status.REJECTED);
                eventRequestRepository.save(eventRequest);
                eventRequestsRejected.add(EventRequestMapper.toEventRequestDto(eventRequest));
            }
            listChangeStatusEventRequestDto.setConfirmedRequests(new ArrayList<>());
            listChangeStatusEventRequestDto.setRejectedRequests(eventRequestsRejected);
            return listChangeStatusEventRequestDto;
        }
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            for (EventRequest eventRequest : eventRequests) {
                eventRequest.setStatus(Status.CONFIRMED);
                eventRequestRepository.save(eventRequest);
                eventRequestsConfirmed.add(EventRequestMapper.toEventRequestDto(eventRequest));
            }
            listChangeStatusEventRequestDto.setConfirmedRequests(eventRequestsConfirmed);
            listChangeStatusEventRequestDto.setRejectedRequests(new ArrayList<>());
            return listChangeStatusEventRequestDto;
        }
        if (eventRequests.size() == 1) {
            if (event.getParticipantLimit() == countEventRequestsConfirmed) {
                throw new ConflictException("Event is full");
            }
        }
        for (EventRequest eventRequest : eventRequests) {
            if (event.getParticipantLimit() > (countEventRequestsConfirmed + i)) {
                eventRequest.setStatus(Status.CONFIRMED);
                eventRequestRepository.save(eventRequest);
                eventRequestsConfirmed.add(EventRequestMapper.toEventRequestDto(eventRequest));
                i++;
            } else {
                eventRequest.setStatus(Status.REJECTED);
                eventRequestRepository.save(eventRequest);
                eventRequestsRejected.add(EventRequestMapper.toEventRequestDto(eventRequest));
            }
        }
        listChangeStatusEventRequestDto.setConfirmedRequests(eventRequestsConfirmed);
        listChangeStatusEventRequestDto.setRejectedRequests(eventRequestsRejected);
        return listChangeStatusEventRequestDto;
    }

    @Override
    @Transactional
    public EventRequestDto createEventRequest(long eventId, long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        validFoundForUser(userOptional, userId);
        User user = userOptional.get();
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        validFoundForEvent(eventOptional, eventId);
        Event event = eventOptional.get();
        if (!event.getState().equals(State.PUBLISHED)) {
            log.info("User with id = {} can't create eventRequest for event with id = {} " +
                    "because it's not PUBLISHED", userId, eventId);
            throw new ConflictException("You can't create eventRequest because it's not PUBLISHED");
        }
        if (event.getState().equals(State.CANCELED)) {
            log.info("User with id = {} can't create eventRequest for event with id = {} " +
                    "because it's CANCELED", userId, eventId);
            throw new ConflictException("You can't create eventRequest because it's CANCELED");
        }
        if (event.getInitiator().getId() == userId) {
            log.info("User with id = {} can't create eventRequest for him event with id = {}", userId, eventId);
            throw new ConflictException("You can't create eventRequest for your Event");
        }
        long confirmedRequest = eventRequestRepository.getCountConfirmedRequest(eventId);
        if ((event.getParticipantLimit() == confirmedRequest) && event.getParticipantLimit() != 0) {
            log.info("User with id = {} can't create eventRequest " +
                    "for this event with id = {} because it's full", userId, eventId);
            throw new ConflictException("For this event participantLimit is full");
        }
        if (!eventRequestRepository.getEventRequestsByRequesterId(userId).isEmpty()) {
            log.info("User with id = {} can't create eventRequest " +
                    "for this event with id = {} because it's second request", userId, eventId);
            throw new ConflictException("You can't send second request for this event");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EventRequest eventRequest = new EventRequest();
        eventRequest.setRequester(user);
        eventRequest.setCreated(LocalDateTime.parse(LocalDateTime.now().format(dateTimeFormatter), dateTimeFormatter));
        if (event.isRequestModeration()) {
            eventRequest.setStatus(Status.PENDING);
        } else {
            eventRequest.setStatus(Status.CONFIRMED);
        }
        if (event.getParticipantLimit() == 0) {
            eventRequest.setStatus(Status.CONFIRMED);
        }
        eventRequest.setEvent(event);
        return EventRequestMapper.toEventRequestDto(eventRequestRepository.save(eventRequest));
    }

    @Override
    public List<EventRequestDto> getEventRequests(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        validFoundForUser(userOptional, userId);
        return eventRequestRepository.getEventRequestsByRequesterId(userId)
                .stream()
                .map(EventRequestMapper::toEventRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestDto canceledEventRequest(long userId, long requestId) {
        Optional<User> userOptional = userRepository.findById(userId);
        validFoundForUser(userOptional, userId);
        Optional<EventRequest> eventRequestOptional = eventRequestRepository.findById(requestId);
        validFoundForEventRequest(eventRequestOptional, requestId);
        EventRequest eventRequest = eventRequestOptional.get();
        if (eventRequest.getRequester().getId() != userId) {
            log.info("User with id = {} can't canceled eventRequest with id = {}", userId, requestId);
            throw new ConflictException("You can't cancel this eventRequest");
        }
        eventRequest.setStatus(Status.CANCELED);
        return EventRequestMapper.toEventRequestDto(eventRequestRepository.save(eventRequest));
    }

    private void validFoundForEventRequest(Optional<EventRequest> eventRequest, long requestId) {
        if (eventRequest.isEmpty()) {
            log.info("EventRequest with id = {} not found", requestId);
            throw new NotFoundException("EventRequest not found");
        }
    }

    private void validFoundForUser(Optional<User> user, long id) {
        if (user.isEmpty()) {
            log.info("User with id = {} not found", id);
            throw new NotFoundException("User not found");
        }
    }

    private void validFoundForEvent(Optional<Event> event, long eventId) {
        if (event.isEmpty()) {
            log.info("Event with id = {} not found", eventId);
            throw new NotFoundException("Event not found");
        }
    }
}
