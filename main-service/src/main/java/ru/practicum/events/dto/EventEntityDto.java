package ru.practicum.events.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import ru.practicum.events.model.Location;
import ru.practicum.valid.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@CheckEventDateAfter2Hours(groups = {Create.class})
public class EventEntityDto {
    private long id;
    @NotBlank(groups = Create.class)
    @Size(min = 20, max = 2000, groups = {Create.class, Update.class, Admin.class})
    private String annotation;
    @NotNull(groups = Create.class)
    private Long category;
    @NotBlank(groups = Create.class)
    @Size(min = 20, max = 7000, groups = {Create.class, Update.class, Admin.class})
    private String description;
    @NotNull(groups = {Create.class})
    private String eventDate;
    @NotNull(groups = Create.class)
    private Location location;
//    @NotNull(groups = Create.class)
    private Boolean paid;
//    @PositiveOrZero(groups = {Create.class})
    private Long participantLimit;
//    @NotNull(groups = Create.class)
    private Boolean requestModeration;
    @NotBlank(groups = Create.class)
    @Size(min = 3, max =120, groups = {Create.class, Update.class, Admin.class})
    private String title;
}
