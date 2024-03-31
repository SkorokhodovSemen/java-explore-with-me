package ru.practicum.events.model;

import ru.practicum.categories.model.Category;
import ru.practicum.categories.model.CategoryMapper;
import ru.practicum.events.dto.*;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class EventMapper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(EventEntityDto eventEntityDto, User user, Category category) {
        Event event = new Event();
        event.setRequestModeration(eventEntityDto.getRequestModeration());
        event.setLon(eventEntityDto.getLocation().getLon());
        event.setLat(eventEntityDto.getLocation().getLat());
        event.setDescription(eventEntityDto.getDescription());
        event.setParticipantLimit(eventEntityDto.getParticipantLimit());
        event.setState(State.PENDING);
        event.setEventDate(LocalDateTime.parse(eventEntityDto.getEventDate(), DATE_TIME_FORMATTER));
        event.setInitiator(user);
        event.setViews(0);
        event.setPaid(eventEntityDto.getPaid());
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER), DATE_TIME_FORMATTER));
        event.setTitle(eventEntityDto.getTitle());
        event.setAnnotation(eventEntityDto.getAnnotation());
        return event;
    }

    public static EventDto toEventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setInitiator(event.getInitiator());
        eventDto.setViews(event.getViews());
        eventDto.setCategory(event.getCategory());
        eventDto.setId(event.getId());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setPaid(event.getPaid());
        eventDto.setPublishedOn(event.getPublishedOn());
        eventDto.setDescription(event.getDescription());
        eventDto.setTitle(event.getTitle());
        Location location = new Location();
        location.setLat(event.getLat());
        location.setLon(event.getLon());
        eventDto.setLocation(location);
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setRequestModeration(event.isRequestModeration());
        eventDto.setState(event.getState());
        return eventDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setId(event.getId());
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toCategoriesDto(event.getCategory()));
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setViews(event.getViews());
        return eventShortDto;
    }

    public static EventFullDto toEventFullDto(Event event, long confirmedRequests) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setInitiator(event.getInitiator());
        eventFullDto.setViews(event.getViews());
        eventFullDto.setCategory(event.getCategory());
        eventFullDto.setId(event.getId());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setTitle(event.getTitle());
        Location location = new Location();
        location.setLat(event.getLat());
        location.setLon(event.getLon());
        eventFullDto.setLocation(location);
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setConfirmedRequests(confirmedRequests);
        eventFullDto.setCreatedOn(event.getCreatedOn());
        return eventFullDto;
    }

    public static EventCreatedDto toEventCreatedDto(Event event) {
        Location location = new Location();
        location.setLat(event.getLat());
        location.setLon(event.getLon());
        EventCreatedDto eventCreatedDto = new EventCreatedDto();
        eventCreatedDto.setInitiator(event.getInitiator());
        eventCreatedDto.setCategory(event.getCategory());
        eventCreatedDto.setId(event.getId());
        eventCreatedDto.setEventDate(event.getEventDate().format(DATE_TIME_FORMATTER));
        eventCreatedDto.setAnnotation(event.getAnnotation());
        eventCreatedDto.setPaid(event.getPaid());
        eventCreatedDto.setCreatedOn(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        eventCreatedDto.setDescription(event.getDescription());
        eventCreatedDto.setTitle(event.getTitle());
        eventCreatedDto.setLocation(location);
        eventCreatedDto.setParticipantLimit(event.getParticipantLimit());
        eventCreatedDto.setRequestModeration(event.isRequestModeration());
        eventCreatedDto.setState(event.getState());
        return eventCreatedDto;
    }

    public static EventAdminSearchDto toEventAdminSearchDto(Event event, long confirmedRequest) {
        EventAdminSearchDto eventAdminSearchDto = new EventAdminSearchDto();
        Location location = new Location();
        location.setLat(event.getLat());
        location.setLon(event.getLon());
        eventAdminSearchDto.setId(event.getId());
        eventAdminSearchDto.setTitle(event.getTitle());
        eventAdminSearchDto.setAnnotation(event.getAnnotation());
        eventAdminSearchDto.setCategory(event.getCategory());
        eventAdminSearchDto.setPaid(event.getPaid());
        eventAdminSearchDto.setEventDate(event.getEventDate().format(DATE_TIME_FORMATTER));
        eventAdminSearchDto.setInitiator(event.getInitiator());
        eventAdminSearchDto.setViews(event.getViews());
        eventAdminSearchDto.setConfirmedRequests(confirmedRequest);
        eventAdminSearchDto.setDescription(event.getDescription());
        eventAdminSearchDto.setParticipantLimit(event.getParticipantLimit());
        eventAdminSearchDto.setRequestModeration(event.isRequestModeration());
        eventAdminSearchDto.setState(event.getState());
        eventAdminSearchDto.setLocation(location);
        eventAdminSearchDto.setCreatedOn(event.getCreatedOn().format(DATE_TIME_FORMATTER));
        if (event.getPublishedOn() != null) {
            eventAdminSearchDto.setPublishedOn(event.getPublishedOn().format(DATE_TIME_FORMATTER));
        }
        return eventAdminSearchDto;
    }

    public static EventAdminPatchDto toEventAdminPatchDto(Event event) {
        EventAdminPatchDto eventAdminPatchDto = new EventAdminPatchDto();
        eventAdminPatchDto.setInitiator(event.getInitiator());
        eventAdminPatchDto.setViews(event.getViews());
        eventAdminPatchDto.setCategory(event.getCategory());
        eventAdminPatchDto.setId(event.getId());
        eventAdminPatchDto.setEventDate(event.getEventDate().format(DATE_TIME_FORMATTER));
        eventAdminPatchDto.setAnnotation(event.getAnnotation());
        eventAdminPatchDto.setPaid(event.getPaid());
        eventAdminPatchDto.setPublishedOn(event.getPublishedOn());
        eventAdminPatchDto.setDescription(event.getDescription());
        eventAdminPatchDto.setTitle(event.getTitle());
        Location location = new Location();
        location.setLat(event.getLat());
        location.setLon(event.getLon());
        eventAdminPatchDto.setLocation(location);
        eventAdminPatchDto.setParticipantLimit(event.getParticipantLimit());
        eventAdminPatchDto.setRequestModeration(event.isRequestModeration());
        eventAdminPatchDto.setState(event.getState());
        eventAdminPatchDto.setCreatedOn(event.getCreatedOn());
        return eventAdminPatchDto;
    }

    public static EventSearchDto toEventSearchDto(Event event, long confirmedRequests) {
        EventSearchDto eventSearchDto = new EventSearchDto();
        eventSearchDto.setInitiator(event.getInitiator());
        eventSearchDto.setViews(event.getViews());
        eventSearchDto.setCategory(event.getCategory());
        eventSearchDto.setId(event.getId());
        eventSearchDto.setEventDate(event.getEventDate().format(DATE_TIME_FORMATTER));
        eventSearchDto.setAnnotation(event.getAnnotation());
        eventSearchDto.setPaid(event.getPaid());
        eventSearchDto.setPublishedOn(event.getPublishedOn());
        eventSearchDto.setDescription(event.getDescription());
        eventSearchDto.setTitle(event.getTitle());
        Location location = new Location();
        location.setLat(event.getLat());
        location.setLon(event.getLon());
        eventSearchDto.setLocation(location);
        eventSearchDto.setParticipantLimit(event.getParticipantLimit());
        eventSearchDto.setRequestModeration(event.isRequestModeration());
        eventSearchDto.setState(event.getState());
        eventSearchDto.setConfirmedRequests(confirmedRequests);
        eventSearchDto.setCreatedOn(event.getCreatedOn());
        return eventSearchDto;
    }
}
