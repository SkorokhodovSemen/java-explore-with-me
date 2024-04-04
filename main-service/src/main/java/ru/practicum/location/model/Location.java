package ru.practicum.location.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "locations")
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float lat;
    private Float lon;
    private String title;
    private String city;
    private String description;
    private Float rad;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
