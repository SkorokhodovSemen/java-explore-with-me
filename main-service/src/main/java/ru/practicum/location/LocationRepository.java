package ru.practicum.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import ru.practicum.location.model.Location;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    ;

    @Procedure(value = "distance")
    List<Float> findRangeToLocation(Float lat1, Float lon1, Float lat2, Float lon2);

    @Query(value = "SELECT l FROM Location l WHERE " +
            "l.title <> 'Unknown'")
    List<Location> findNotUnknownLocation();

    @Query(value = "SELECT l FROM Location l WHERE " +
            "l.id IN ?1")
    List<Location> findLocationByIds(List<Long> ids);
}
