package ru.practicum.compilations;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.ResponseCompilationDto;

import java.util.List;

public interface CompilationService {
    void deleteCompilationById(long compId);

    CompilationDto createCompilation(ResponseCompilationDto responseCompilationDto);

    CompilationDto updateCompilationById(ResponseCompilationDto responseCompilationDto, long compId);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationsById(long compId);
}

