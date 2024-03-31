package ru.practicum.compilations.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.valid.Create;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@RequiredArgsConstructor
public class PostCompilationDto {
    private List<Long> events;
    @NotNull(groups = Create.class)
    private Boolean pinned;
    @NotNull(groups = Create.class)
    private String title;
}
