package ru.practicum.events.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Location {
    private Float lat;
    private Float lon;
}
