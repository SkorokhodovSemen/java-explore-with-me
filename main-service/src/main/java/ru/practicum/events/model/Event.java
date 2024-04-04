package ru.practicum.events.model;

import lombok.*;
import ru.practicum.categories.model.Category;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    private Boolean paid;
    @Column(name = "participant_limit")
    private long participantLimit;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    private String title;
    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;
    private long views;
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;
    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return id == event.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
