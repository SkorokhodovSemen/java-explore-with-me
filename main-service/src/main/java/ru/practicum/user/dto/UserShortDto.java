package ru.practicum.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserShortDto {
    private long id;
    private String name;
}
