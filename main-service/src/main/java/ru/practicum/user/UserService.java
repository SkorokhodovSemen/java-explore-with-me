package ru.practicum.user;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    void deleteUserById(Long id);

    UserDto createUser(UserDto userDto);
}
