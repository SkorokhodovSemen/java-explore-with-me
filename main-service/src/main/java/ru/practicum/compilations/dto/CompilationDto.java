package ru.practicum.compilations.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.events.dto.EventShortDto;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CompilationDto {
    private long id;
    private Boolean pinned;
    private String title;
    private List<EventShortDto> events;
}
