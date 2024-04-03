package ru.practicum.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitsDto;
import ru.practicum.client.StatsClient;
import ru.practicum.events.dto.EventAdminSearchDto;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventSearchDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping
    public List<EventAdminSearchDto> getEventsForQuery(@RequestParam(name = "text", defaultValue = "") String text,
                                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                       @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                                       @RequestParam(name = "paid", required = false) Boolean paid,
                                                       @RequestParam(name = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                                       @RequestParam(name = "categories", defaultValue = "") List<Long> categories,
                                                       @RequestParam(name = "rangeStart", defaultValue = "#{T(java.time.LocalDateTime).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                       @RequestParam(name = "rangeEnd", defaultValue = "2500-01-01 00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                       @RequestParam(name = "sort", defaultValue = "VIEWS") String sort,
                                                       HttpServletRequest httpServletRequest) {
        log.info("Get event with text = {}\n" +
                "paid = {}\n" +
                "onlyAvailable = {}\n" +
                "categories = {}\n" +
                "rangeStart = {}\n" +
                "rangeEnd = {}\n" +
                "sort = {}", text, paid, onlyAvailable, categories, rangeStart, rangeEnd, sort);
        EndpointHitsDto endpointHitsDto = new EndpointHitsDto();
        endpointHitsDto.setIp(httpServletRequest.getRemoteAddr());
        endpointHitsDto.setApp("ewm-main-service");
        endpointHitsDto.setUri(httpServletRequest.getRequestURI());
        endpointHitsDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statsClient.create(endpointHitsDto);
        return eventService.getEventsForQuery(text, paid, onlyAvailable, categories, rangeStart, rangeEnd, sort, from, size, httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
    }

    @GetMapping("/{id}")
    public EventSearchDto getEventsByIdForQuery(@PathVariable(name = "id") long id, HttpServletRequest httpServletRequest) {
        log.info("Get event with id = {}", id);
        EndpointHitsDto endpointHitsDto = new EndpointHitsDto();
        endpointHitsDto.setIp(httpServletRequest.getRemoteAddr());
        endpointHitsDto.setApp("ewm-main-service");
        endpointHitsDto.setUri(httpServletRequest.getRequestURI());
        endpointHitsDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statsClient.create(endpointHitsDto);
        return eventService.getEventsByIdForQuery(id, httpServletRequest.getRequestURI());
    }

    @GetMapping("/locations/{locId}")
    public List<EventDto> getEventsByLocationId(@PathVariable("locId") long locId,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                                HttpServletRequest httpServletRequest) {
        log.info("Get events with locId = {}", locId);
        EndpointHitsDto endpointHitsDto = new EndpointHitsDto();
        endpointHitsDto.setIp(httpServletRequest.getRemoteAddr());
        endpointHitsDto.setApp("ewm-main-service");
        endpointHitsDto.setUri(httpServletRequest.getRequestURI());
        endpointHitsDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statsClient.create(endpointHitsDto);
        return eventService.getEventsByLocationId(locId, from, size);
    }

    @GetMapping("/locations")
    public List<EventDto> getEventsByLocationTitle(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                                   @RequestParam(name = "title") String title,
                                                   HttpServletRequest httpServletRequest) {
        log.info("Get events with location title = {}", title);
        EndpointHitsDto endpointHitsDto = new EndpointHitsDto();
        endpointHitsDto.setIp(httpServletRequest.getRemoteAddr());
        endpointHitsDto.setApp("ewm-main-service");
        endpointHitsDto.setUri(httpServletRequest.getRequestURI());
        endpointHitsDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statsClient.create(endpointHitsDto);
        return eventService.getEventsByLocationTitle(title, from, size);
    }
}
