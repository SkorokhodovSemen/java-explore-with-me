package ru.practicum.compilations.model;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.PostCompilationDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CompilationMapper {
    public static Compilation toCompilation(PostCompilationDto postCompilationDto, List<Event> events){
        Compilation compilation = new Compilation();
        compilation.setPinned(postCompilationDto.getPinned());
        compilation.setTitle(postCompilationDto.getTitle());
        compilation.setEvents(events);
        return compilation;
    }

    public static CompilationDto toCompilationDto (Compilation compilation){
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        List<EventShortDto> eventShortDtos = compilation
                .getEvents()
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        compilationDto.setEvents(eventShortDtos);
        return compilationDto;
    }
}
