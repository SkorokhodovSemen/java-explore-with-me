package ru.practicum.compilations.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@RequiredArgsConstructor
public class PostCompilationDto {
    private long id;
    private List<Long> events;
    private Boolean pinned;
    @NotBlank(groups = {Create.class})
    @Size(min = 2, max = 50, groups = {Update.class, Create.class})
    private String title;
}
