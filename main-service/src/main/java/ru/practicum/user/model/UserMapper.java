package ru.practicum.user.model;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;

public abstract class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        if (userDto.getId() != 0) {
            user.setId(userDto.getId());
        }
        return user;
    }

    public static UserShortDto toUserShortDto(User user){
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }
}
