package ru.practicum.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStatsDto;
import ru.practicum.categories.CategoryRepository;
import ru.practicum.categories.model.Category;
import ru.practicum.client.StatsClient;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventMapper;
import ru.practicum.events.model.State;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.location.LocationRepository;
import ru.practicum.location.model.Location;
import ru.practicum.request.EventRequestRepository;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EventRequestRepository eventRequestRepository;
    private final StatsClient statsClient;
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public EventCreatedDto createEvent(EventEntityDto eventEntityDto, long userId) {
        if (eventEntityDto.getParticipantLimit() != null && eventEntityDto.getParticipantLimit() < 0) {
            throw new ValidationException("ParticipantLimit must be positive");
        }
        Category category = categoryRepository.findById(eventEntityDto.getCategory()).orElseThrow(() -> {
            log.info("Category with id = {} not found", eventEntityDto.getCategory());
            return new NotFoundException("Category not found");
        });
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id = {} not found", userId);
            return new NotFoundException("User not found");
        });
        if (eventEntityDto.getPaid() == null) {
            eventEntityDto.setPaid(false);
        }
        if (eventEntityDto.getParticipantLimit() == null) {
            eventEntityDto.setParticipantLimit(0L);
        }
        if (eventEntityDto.getRequestModeration() == null) {
            eventEntityDto.setRequestModeration(true);
        }
        Location locationToCreate = new Location();
        List<Location> locationsTest = locationRepository.findNotUnknownLocation();
        long i = 0;
        if (locationsTest.size() != 0) {
            for (Location location : locationsTest) {
                float distance = locationRepository.findRangeToLocation(eventEntityDto.getLocation().getLat(),
                        eventEntityDto.getLocation().getLon(),
                        location.getLat(),
                        location.getLon()).get(0);
                if (distance < location.getRad()) {
                    locationToCreate = location;
                    i++;
                    break;
                }
            }
        }
        if (locationsTest.size() == 0 || i == 0) {
            locationToCreate.setCity("Unknown");
            locationToCreate.setLat(eventEntityDto.getLocation().getLat());
            locationToCreate.setLon(eventEntityDto.getLocation().getLon());
            locationToCreate.setRad(0F);
            locationToCreate.setDescription("Not description");
            locationToCreate.setTitle("Unknown");
            locationToCreate = locationRepository.save(locationToCreate);
        }
        return EventMapper.toEventCreatedDto(eventRepository.save(EventMapper.toEvent(eventEntityDto,
                user,
                category,
                locationToCreate)));
    }

    @Override
    public List<EventDto> getEvents(long userId, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id = {} not found", userId);
            return new NotFoundException("User not found");
        });
        return eventRepository.getAllEventsByUserId(userId, page)
                .getContent()
                .stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(long userId, long eventId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id = {} not found", userId);
            return new NotFoundException("User not found");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Event with id = {} not found", eventId);
            return new NotFoundException("Event not found");
        });
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Event with id = " + eventId + " not found for user with id = " + userId);
        } else {
            return EventMapper.toEventFullDto(event, eventRequestRepository.getCountConfirmedRequest(eventId));
        }
    }

    @Override
    @Transactional
    public EventAdminPatchDto updateEvent(EventAdminDto eventAdminDto, long userId, long eventId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id = {} not found", userId);
            return new NotFoundException("User not found");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Event with id = {} not found", eventId);
            return new NotFoundException("Event not found");
        });
        if (eventAdminDto.getParticipantLimit() != null && eventAdminDto.getParticipantLimit() < 0) {
            throw new ValidationException("ParticipantLimit must be positive");
        }
        if (eventAdminDto.getEventDate() != null) {
            if (LocalDateTime.parse(eventAdminDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("Start is before 2 hours");
            }
        }
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Event with id = " + eventId + " not found for user with id = " + userId);
        }
        if (eventAdminDto.getCategory() != null) {
            categoryRepository.findById(eventAdminDto.getCategory()).orElseThrow(() -> {
                log.info("Category with id = {} not found", eventAdminDto.getCategory());
                return new NotFoundException("Category not found");
            });
        }
        validStatusForEvent(event, eventId);
        if (eventAdminDto.getStateAction() != null) {
            if (eventAdminDto.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
                return EventMapper.toEventAdminPatchDto(eventRepository.save(updateFieldsEvent(event,
                        eventAdminDto)));
            }
        }
        event.setState(State.PENDING);
        return EventMapper.toEventAdminPatchDto(eventRepository.save(updateFieldsEvent(event,
                eventAdminDto)));
    }

    @Override
    public List<EventAdminSearchDto> getEventsForAdmin(List<Long> users,
                                                       List<State> states,
                                                       List<Long> categories,
                                                       LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd,
                                                       int from,
                                                       int size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Event> events;
        if (users.size() == 0) {
            if (states.size() == 0) {
                if (categories.size() == 0) {
                    events = eventRepository.getEventsForAdminWithoutParameters(rangeStart, rangeEnd, page).getContent();
                } else {
                    events = eventRepository.getEventsForAdminOnlyCategories(categories, rangeStart, rangeEnd, page).getContent();
                }
            } else {
                if (categories.size() == 0) {
                    events = eventRepository.getEventsForAdminOnlyStates(states, rangeStart, rangeEnd, page).getContent();
                } else {
                    events = eventRepository.getEventsForAdminWithCategoriesAndStates(categories, states, rangeStart, rangeEnd, page).getContent();
                }
            }
        } else {
            if (states.size() == 0) {
                if (categories.size() == 0) {
                    events = eventRepository.getEventsForAdminOnlyUsers(users, rangeStart, rangeEnd, page).getContent();
                } else {
                    events = eventRepository.getEventsForAdminWithUsersAndCategories(users, categories, rangeStart, rangeEnd, page).getContent();
                }
            } else {
                if (categories.size() == 0) {
                    events = eventRepository.getEventsForAdminWithUsersAndStates(users, states, rangeStart, rangeEnd, page).getContent();
                } else {
                    events = eventRepository.getEventsForAdminWithAllParameters(users, states, categories, rangeStart, rangeEnd, page).getContent();
                }
            }
        }
        List<EventAdminSearchDto> eventAdminSearchDtos = new ArrayList<>();
        for (Event event : events) {
            eventAdminSearchDtos
                    .add(EventMapper.toEventAdminSearchDto(event,
                            eventRequestRepository.getCountConfirmedRequest(event.getId())));
        }
        return eventAdminSearchDtos.stream().sorted().collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventAdminPatchDto updateEventByIdForAdmin(EventAdminDto eventAdminDto, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Event with id = {} not found", eventId);
            return new NotFoundException("Event not found");
        });
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Event is end");
        }
        if (eventAdminDto.getEventDate() != null) {
            if (LocalDateTime.parse(eventAdminDto.getEventDate(), dateTimeFormatter).isBefore(LocalDateTime.now())) {
                throw new ValidationException("Event is end");
            }
        }
        if (eventAdminDto.getStateAction() != null) {
            if (event.getState().equals(State.PENDING) && eventAdminDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.parse(LocalDateTime.now().format(dateTimeFormatter), dateTimeFormatter));
                Event eventUpdate = updateFieldsEvent(event, eventAdminDto);
                return EventMapper.toEventAdminPatchDto(eventRepository.save(eventUpdate));
            }
            if (event.getState().equals(State.PENDING) && eventAdminDto.getStateAction().equals(StateAction.REJECT_EVENT)) {
                event.setPublishedOn(LocalDateTime.parse(LocalDateTime.now().format(dateTimeFormatter), dateTimeFormatter));
                event.setState(State.CANCELED);
                Event eventUpdate = updateFieldsEvent(event, eventAdminDto);
                return EventMapper.toEventAdminPatchDto(eventRepository.save(eventUpdate));
            }
            if (eventAdminDto.getStateAction().equals(StateAction.PUBLISH_EVENT) && event.getState().equals(State.CANCELED)) {
                throw new ConflictException("Event must have State.PENDING");
            }
            if (event.getState().equals(State.PUBLISHED)) {
                throw new ConflictException("Event must be not PUBLISHED");
            }
        }
        Event eventUpdate = updateFieldsEvent(event, eventAdminDto);
        return EventMapper.toEventAdminPatchDto(eventRepository.save(eventUpdate));
    }

    @Override
    @Transactional
    public EventSearchDto getEventsByIdForQuery(long id, String uri) {
        Event event = eventRepository.findById(id).orElseThrow(() -> {
            log.info("Event with id = {} not found", id);
            return new NotFoundException("Event not found");
        });
        if (!event.getState().equals(State.PUBLISHED)) {
            log.info("Event with id = {} must have State.PUBLISHED", id);
            throw new NotFoundException("Event not Published");
        }
        long confirmedRequests = eventRequestRepository.getCountConfirmedRequest(id);
        updateViewsForOneEvent(id, uri);
        return EventMapper.toEventSearchDto(event, confirmedRequests);
    }

    @Override
    @Transactional
    public List<EventAdminSearchDto> getEventsForQuery(String text,
                                                       Boolean paid,
                                                       boolean onlyAvailable,
                                                       List<Long> categories,
                                                       LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd,
                                                       String sort,
                                                       int from,
                                                       int size,
                                                       String uri,
                                                       String ip) {
        Pageable pageable = null;
        if (sort.equals(EventSort.EVENT_DATE.toString())) {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "eventDate"));
        }
        if (sort.equals(EventSort.VIEWS.toString())) {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "views"));
        }
        if (rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("End must be after start");
        }
        if (pageable == null) {
            log.info("Sort = {} is not exist", sort);
            throw new ConflictException("Sort is not exist");
        }
        List<EventAdminSearchDto> eventAdminSearchDtos = new ArrayList<>();
        List<Event> events;
        if (onlyAvailable) {
            if (categories.size() == 0) {
                events = eventRepository.getEventsAvailableTrueCategoriesNotExist(text, paid, rangeStart, rangeEnd, pageable)
                        .getContent();
            } else {
                events = eventRepository.getEventsAvailableTrueCategoriesExist(text, paid, categories, rangeStart, rangeEnd, pageable)
                        .getContent();
            }
        } else {
            if (categories.size() == 0) {
                events = eventRepository.getEventsAvailableFalseCategoriesNotExist(text, paid, rangeStart, rangeEnd, pageable)
                        .getContent();
            } else {
                events = eventRepository.getEventsAvailableFalseCategoriesExist(text, paid, categories, rangeStart, rangeEnd, pageable)
                        .getContent();
            }
        }
        for (Event event : events) {
            eventAdminSearchDtos
                    .add(EventMapper.toEventAdminSearchDto(event,
                            eventRequestRepository.getCountConfirmedRequest(event.getId())));
        }
        return eventAdminSearchDtos;
    }

    @Override
    public List<EventDto> getEventsByLocationId(long locId, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        locationRepository.findById(locId).orElseThrow(() -> {
            log.info("Location with id = {} not found", locId);
            return new NotFoundException("Location not found");
        });
        return eventRepository.getEventsByLocationId(locId, page).getContent()
                .stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDto> getEventsByLocationTitle(String title, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        return eventRepository.getEventsByLocationTitle(title, page).getContent()
                .stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }

    private void updateViewsForOneEvent(long id, String uri) {
        Event event = eventRepository.findById(id).get();
        List<String> uris = new ArrayList<>();
        uris.add(uri);
        List<ViewStatsDto> viewStatsDtos = statsClient.getStats(LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100), uris, true).getBody();
        event.setViews(viewStatsDtos.get(0).getHits());
        eventRepository.save(event);
    }

    private void validStatusForEvent(Event event, long eventId) {
        if (event.getState().equals(State.PUBLISHED)) {
            log.info("You can't change Event with id = {}. Event is PUBLISHED", eventId);
            throw new ConflictException("You can't change Event. Event is PUBLISHED");
        }
    }

    private Event updateFieldsEvent(Event event, EventEntityDto eventEntityDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (eventEntityDto.getAnnotation() != null) {
            event.setAnnotation(eventEntityDto.getAnnotation());
        }
        if (eventEntityDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventEntityDto.getCategory()).get());
        }
        if (eventEntityDto.getDescription() != null) {
            event.setDescription(eventEntityDto.getDescription());
        }
        if (eventEntityDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(eventEntityDto.getEventDate(), formatter));
        }
        if (eventEntityDto.getLocation() != null) {
            Location locationToCreate = new Location();
            List<Location> locationsTest = locationRepository.findNotUnknownLocation();
            long i = 0;
            if (locationsTest.size() != 0) {
                for (Location location : locationsTest) {
                    Float distance = locationRepository.findRangeToLocation(eventEntityDto.getLocation().getLat(),
                            eventEntityDto.getLocation().getLon(),
                            location.getLat(),
                            location.getLon()).get(0);
                    if (distance < location.getRad()) {
                        locationToCreate = location;
                        i++;
                        break;
                    }
                }
            }
            if (locationsTest.size() == 0 || i == 0) {
                locationToCreate.setCity("Unknown");
                locationToCreate.setLat(eventEntityDto.getLocation().getLat());
                locationToCreate.setLon(eventEntityDto.getLocation().getLon());
                locationToCreate.setRad(0F);
                locationToCreate.setDescription("Not description");
                locationToCreate.setTitle("Unknown");
                locationToCreate = locationRepository.save(locationToCreate);
            }
            event.setLocation(locationToCreate);
        }
        if (eventEntityDto.getPaid() != null) {
            event.setPaid(eventEntityDto.getPaid());
        }
        if (eventEntityDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventEntityDto.getParticipantLimit());
        }
        if (eventEntityDto.getRequestModeration() != null) {
            event.setRequestModeration(eventEntityDto.getRequestModeration());
        }
        if (eventEntityDto.getTitle() != null) {
            event.setTitle(eventEntityDto.getTitle());
        }
        return event;
    }
}
