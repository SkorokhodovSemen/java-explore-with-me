package ru.practicum.events.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EventAdminDto extends EventEntityDto{
    private String publishedOn;
    private StateAction stateAction;
}
