package ru.practicum.events.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class EventFullDto extends EventDto {
    private long confirmedRequests;
    private LocalDateTime createdOn;
}
