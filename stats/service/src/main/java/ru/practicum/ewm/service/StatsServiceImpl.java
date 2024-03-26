package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EndpointHitsDto;
import ru.practicum.ewm.model.EndpointHitsMapper;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.repository.StatsRepository;

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
