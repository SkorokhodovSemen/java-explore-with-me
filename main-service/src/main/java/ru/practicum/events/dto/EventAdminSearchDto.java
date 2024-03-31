package ru.practicum.events.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.categories.model.Category;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.State;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class EventAdminSearchDto implements Comparable<EventAdminSearchDto> {
    private long id;
    private String title;
    private String annotation;
    private Category category;
    private Boolean paid;
    private String eventDate;
    private User initiator;
    private long views;
    private long confirmedRequests;
    private String description;
    private long participantLimit;
    private boolean requestModeration;
    private State state;
    private String createdOn;
    private String publishedOn;
    private Location location;

    @Override
    public int compareTo(EventAdminSearchDto o) {
        return (int) (this.getId() - o.getId());
    }
}
