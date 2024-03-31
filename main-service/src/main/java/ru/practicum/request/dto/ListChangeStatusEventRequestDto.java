package ru.practicum.request.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.request.dto.EventRequestDto;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ListChangeStatusEventRequestDto {
    private List<EventRequestDto> confirmedRequests;
    private List<EventRequestDto> rejectedRequests;
}
