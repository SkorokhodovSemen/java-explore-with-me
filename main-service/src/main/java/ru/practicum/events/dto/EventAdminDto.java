package ru.practicum.events.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.valid.Admin;
import ru.practicum.valid.CheckEventDatePublishedAfter1Hours;
import ru.practicum.valid.Update;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
//@CheckEventDatePublishedAfter1Hours(groups = Admin.class)
public class EventAdminDto extends EventEntityDto{
    private String publishedOn;
//    @NotNull(groups = Update.class)
    private StateAction stateAction;
}
