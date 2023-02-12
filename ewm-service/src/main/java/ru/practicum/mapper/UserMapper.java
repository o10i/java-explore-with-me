package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static User toUser(UserRequestDto userRequestDto) {
        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        return user;
    }
}
