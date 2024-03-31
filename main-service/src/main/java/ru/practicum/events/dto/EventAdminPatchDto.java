package ru.practicum.events.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.categories.model.Category;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.State;
import ru.practicum.user.model.User;
import ru.practicum.valid.Update;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class EventAdminPatchDto {
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
    private long views;
    private State state;
    private LocalDateTime publishedOn;
    private LocalDateTime createdOn;
}
