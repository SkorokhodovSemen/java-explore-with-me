package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitsDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                       @RequestParam(name = "uris", defaultValue = "") List<String> uris,
                                       @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("Get stats with " +
                "start = {}, " +
                "end = {}, " +
                "uris = {}, " +
                "unique = {}", start, end, uris, unique);
        return service.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public EndpointHitsDto create(@RequestBody EndpointHitsDto endpointHitsDto) {
        log.info("Create new EndpointHit = {}", endpointHitsDto);
        return service.create(endpointHitsDto);
    }
}
