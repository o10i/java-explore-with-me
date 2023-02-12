package ru.practicum.service;

import ru.practicum.dto.EventRequestDto;
import ru.practicum.model.Event;

public interface EventService {
    Event save(EventRequestDto eventRequestDto, Long userId);
}
