package ru.practicum.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.PostCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.model.CompilationMapper;
import ru.practicum.events.EventRepository;
import ru.practicum.events.model.Event;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService{
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
    public CompilationDto createCompilation(PostCompilationDto postCompilationDto) {
        List<Event> events = eventRepository.getEventsForCompilation(postCompilationDto.getEvents());
        return CompilationMapper.toCompilationDto(CompilationMapper.toCompilation(postCompilationDto, events));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilationById(PostCompilationDto postCompilationDto, long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        validFoundForCompilation(compilationOptional, compId);
        Compilation compilation = compilationOptional.get();
        if (postCompilationDto.getTitle() != null){
            compilation.setTitle(postCompilationDto.getTitle());
        }
        if (postCompilationDto.getPinned() != null){
            compilation.setPinned(postCompilationDto.getPinned());
        }
        if (postCompilationDto.getEvents() != null){
            List<Event> events = eventRepository.getEventsForCompilation(postCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    private void validFoundForCompilation(Optional<Compilation> compilationOptional, long compId){
        if (compilationOptional.isEmpty()){
            log.info("Compilation with id = {} not found", compId);
            throw new NotFoundException("Compilation not found");
        }
    }
}
