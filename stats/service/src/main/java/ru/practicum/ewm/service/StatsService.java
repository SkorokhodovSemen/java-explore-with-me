package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHitsDto;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    EndpointHitsDto create(EndpointHitsDto endpointHitsDto);
}
