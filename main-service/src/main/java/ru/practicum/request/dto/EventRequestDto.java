package ru.practicum.request.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.request.model.Status;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class EventRequestDto {
    private LocalDateTime created;
    private long event;
    private long id;
    private long requester;
    private Status status;
}
