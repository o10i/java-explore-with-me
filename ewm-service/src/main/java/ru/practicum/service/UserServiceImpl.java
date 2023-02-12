package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.UserMapper.toUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<User> getAll(List<Long> ids, Integer from, Integer size) {
        if (ids == null) {
            return repository.findAll().stream().skip(from).limit(size).collect(Collectors.toList());
        } else {
            return repository.findAllById(ids);
        }
    }

    @Override
    public User save(UserRequestDto userRequestDto) {
        return repository.save(toUser(userRequestDto));
    }

    @Override
    public void delete(Long userId) {
        getByIdWithCheck(userId);
        repository.deleteById(userId);
    }

    public void getByIdWithCheck(Long userId) {
        repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
    }
}
