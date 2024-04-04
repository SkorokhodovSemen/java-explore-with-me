package ru.practicum.events.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.valid.*;

import javax.validation.constraints.*;

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
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    @NotBlank(groups = Create.class)
    @Size(min = 3, max = 120, groups = {Create.class, Update.class, Admin.class})
    private String title;
}
