package ru.practicum.compilations;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.PostCompilationDto;

public interface CompilationService {
    void deleteCompilationById(long compId);

    CompilationDto createCompilation(PostCompilationDto postCompilationDto);

    CompilationDto updateCompilationById(PostCompilationDto postCompilationDto, long compId);
}

