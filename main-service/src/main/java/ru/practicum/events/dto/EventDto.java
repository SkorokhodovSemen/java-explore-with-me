package ru.practicum.events.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import ru.practicum.categories.model.Category;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.State;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class EventDto {
    private long id;
    private String annotation;
    private Category category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private long participantLimit;
    private boolean requestModeration;
    private String title;
    private User initiator;
    private long views;
    private State state;
    private LocalDateTime publishedOn;
}
