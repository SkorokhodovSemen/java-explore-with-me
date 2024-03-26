package ru.practicum.model;

import ru.practicum.dto.EndpointHitsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class EndpointHitsMapper {
    public static EndpointHitsDto toEndpointHitsDto(EndpointHit endpointHit) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EndpointHitsDto endpointHitsDto = new EndpointHitsDto();
        endpointHitsDto.setApp(endpointHit.getApp());
        endpointHitsDto.setIp(endpointHit.getIp());
        endpointHitsDto.setUri(endpointHit.getUri());
        endpointHitsDto.setId(endpointHit.getId());
        endpointHitsDto.setTimestamp(endpointHit.getTimestamp().format(formatter));
        return endpointHitsDto;
    }

    public static EndpointHit toEndpointHits(EndpointHitsDto endpointHitsDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(endpointHitsDto.getApp());
        endpointHit.setIp(endpointHitsDto.getIp());
        endpointHit.setTimestamp(LocalDateTime.parse(endpointHitsDto.getTimestamp(), formatter));
        endpointHit.setUri(endpointHitsDto.getUri());
        return endpointHit;
    }
}
