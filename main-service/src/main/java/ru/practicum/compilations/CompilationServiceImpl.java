package ru.practicum.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.ResponseCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.model.CompilationMapper;
import ru.practicum.events.EventRepository;
import ru.practicum.events.model.Event;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void deleteCompilationById(long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        validFoundForCompilation(compilationOptional, compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto createCompilation(ResponseCompilationDto responseCompilationDto) {
        List<Event> events = eventRepository.getEventsForCompilation(responseCompilationDto.getEvents());
        if (responseCompilationDto.getPinned() == null) {
            responseCompilationDto.setPinned(false);
        }
        return CompilationMapper.toCompilationDto(compilationRepository
                .save(CompilationMapper.toCompilation(responseCompilationDto, events)));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilationById(ResponseCompilationDto responseCompilationDto, long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        validFoundForCompilation(compilationOptional, compId);
        Compilation compilation = compilationOptional.get();
        if (responseCompilationDto.getTitle() != null) {
            compilation.setTitle(responseCompilationDto.getTitle());
        }
        if (responseCompilationDto.getPinned() != null) {
            compilation.setPinned(responseCompilationDto.getPinned());
        }
        if (responseCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.getEventsForCompilation(responseCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        if (pinned == null) {
            return compilationRepository.findAll(page)
                    .getContent()
                    .stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        } else if (pinned.equals(true)) {
            return compilationRepository.getCompilationWithTruePinned(page).getContent()
                    .stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.getCompilationWithFalsePinned(page).getContent()
                    .stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public CompilationDto getCompilationsById(long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        validFoundForCompilation(compilationOptional, compId);
        return CompilationMapper.toCompilationDto(compilationOptional.get());
    }

    private void validFoundForCompilation(Optional<Compilation> compilationOptional, long compId) {
        if (compilationOptional.isEmpty()) {
            log.info("Compilation with id = {} not found", compId);
            throw new NotFoundException("Compilation not found");
        }
    }
}
