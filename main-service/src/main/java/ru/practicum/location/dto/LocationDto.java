package ru.practicum.location.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
public class LocationDto {
    private Long id;
    @NotNull(groups = Create.class)
    private Float lat;
    @NotNull(groups = Create.class)
    private Float lon;
    @NotBlank(message = "Title is blank", groups = Create.class)
    @Size(max = 250, min = 2, groups = {Create.class, Update.class})
    private String title;
    @NotBlank(message = "City is blank", groups = Create.class)
    @Size(max = 100, min = 2, groups = {Create.class, Update.class})
    private String city;
    @NotBlank(message = "Description is blank", groups = Create.class)
    @Size(max = 2000, min = 20, groups = {Create.class, Update.class})
    private String description;
    @NotNull(groups = Create.class)
    private Float rad;
}
