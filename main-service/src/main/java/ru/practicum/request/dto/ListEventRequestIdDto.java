package ru.practicum.request.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ListEventRequestIdDto {
    private List<Long> requestIds;
    private String status;
}
