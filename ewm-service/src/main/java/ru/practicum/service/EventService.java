package ru.practicum.service;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.NewEventDto;

public interface EventService {
    EventFullDto save(Long userId, NewEventDto newEventDto);
}
