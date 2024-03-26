package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitsDto;
import ru.practicum.client.StatsClient;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsClient statsClient;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                           @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam(name = "uris", defaultValue = "") List<String> uris,
                                           @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("Get stats with " +
                "start = {}, " +
                "end = {}, " +
                "uris = {}, " +
                "unique = {}", start, end, uris, unique);
        return statsClient.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> create(@RequestBody EndpointHitsDto endpointHitsDto) {
        log.info("Create new EndpointHit = {}", endpointHitsDto);
        return statsClient.create(endpointHitsDto);
    }
}
