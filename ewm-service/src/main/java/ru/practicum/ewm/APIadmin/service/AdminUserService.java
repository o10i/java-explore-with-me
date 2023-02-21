package ru.practicum.ewm.APIadmin.service;

import ru.practicum.ewm.common.dto.NewUserRequest;
import ru.practicum.ewm.common.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    UserDto save(NewUserRequest newUserRequest);

    void delete(Long userId);
}
