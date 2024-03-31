package ru.practicum.compilations.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.events.model.Event;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToMany
    @JoinTable(name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilations_id"),
            inverseJoinColumns = @JoinColumn(name = "events_id"))
    private List<Event> events;
    private Boolean pinned;
    private String title;
}
