package ru.practicum.request.model;

import lombok.*;
import ru.practicum.events.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "requests")
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class EventRequest {
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "requester")
    private User requester;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventRequest)) return false;
        EventRequest that = (EventRequest) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
