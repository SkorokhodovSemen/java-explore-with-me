package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private long hits;
}
