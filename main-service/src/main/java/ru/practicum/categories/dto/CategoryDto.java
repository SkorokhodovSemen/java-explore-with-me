package ru.practicum.categories.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
public class CategoryDto {
    private long id;
    @NotBlank(message = "Email is blank", groups = {Create.class, Update.class})
    @Size(max = 50, groups = {Create.class, Update.class})
    private String name;
}
