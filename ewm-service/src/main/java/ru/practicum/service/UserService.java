package ru.practicum.service;

import ru.practicum.dto.UserRequestDto;
import ru.practicum.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll(List<Long> ids, Integer from, Integer size);

    User save(UserRequestDto userRequestDto);

    void delete(Long userId);
}
