package ru.practicum.events.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.categories.model.Category;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.State;
import ru.practicum.user.model.User;

@Data
@RequiredArgsConstructor
public class EventCreatedDto {
    private long id;
    private String annotation;
    private Category category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private long participantLimit;
    private boolean requestModeration;
    private String title;
    private User initiator;
    private State state;
    private String createdOn;
}
