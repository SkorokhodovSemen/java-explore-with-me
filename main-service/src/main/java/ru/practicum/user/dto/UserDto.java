package ru.practicum.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
public class UserDto {
    private long id;
    @NotBlank(message = "Email is blank", groups = Create.class)
    @Size(max = 250, min = 2, groups = {Create.class, Update.class})
    private String name;
    @NotBlank(message = "Email is blank", groups = Create.class)
    @Email(message = "Email is incorrect", groups = Create.class)
    @Size(max = 254, min = 6, groups = {Create.class, Update.class})
    private String email;
}
