package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitsDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.ValidationExceptionStats;
import ru.practicum.model.EndpointHitsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start == null || end == null) {
            throw new ValidationExceptionStats("Start or end can't be null");
        }
        if (start.isAfter(end)) {
            throw new ValidationExceptionStats("Start can't be after end");
        }
        if (unique) {
            if (uris.isEmpty()) {
                return repository.getStatsWhereUniqueTrueUrisEmpty(start, end);
            } else {
                return repository.getStatsWhereUniqueTrueUrisExist(start, end, uris);
            }
        } else {
            if (uris.isEmpty()) {
                return repository.getStatsWhereUniqueFalseUrisEmpty(start, end);
            } else {
                return repository.getStatsWhereUniqueFalseUrisExist(start, end, uris);
            }
        }
    }

    @Override
    @Transactional
    public EndpointHitsDto create(EndpointHitsDto endpointHitsDto) {
        return EndpointHitsMapper
                .toEndpointHitsDto(repository.save(EndpointHitsMapper.toEndpointHits(endpointHitsDto)));
    }
}
