package ru.practicum.compilations;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.PostCompilationDto;
import ru.practicum.compilations.model.Compilation;

import java.util.List;

public interface CompilationService {
    void deleteCompilationById(long compId);

    CompilationDto createCompilation(PostCompilationDto postCompilationDto);

    CompilationDto updateCompilationById(PostCompilationDto postCompilationDto, long compId);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationsById(long compId);
}

