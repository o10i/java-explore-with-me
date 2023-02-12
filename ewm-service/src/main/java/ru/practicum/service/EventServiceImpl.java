package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventRequestDto;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;

import static ru.practicum.mapper.EventMapper.toEvent;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService  {
    private final EventRepository repository;

    @Override
    public Event save(EventRequestDto eventRequestDto, Long userId) {
        return repository.save(toEvent(eventRequestDto));
    }
}
