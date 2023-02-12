package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;

import static ru.practicum.mapper.DateTimeMapper.toLocalDateTime;
import static ru.practicum.mapper.EventMapper.toEvent;
import static ru.practicum.mapper.EventMapper.toEventFullDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService  {
    private final EventRepository repository;
    private final CategoryServiceImpl categoryService;
    private final UserServiceImpl userService;

    @Transactional
    @Override
    public EventFullDto save(Long userId, NewEventDto newEventDto) {
        checkEventDate(newEventDto);
        Event event = toEvent(newEventDto);
        event.setCategory(categoryService.getByIdWithCheck(newEventDto.getCategory()));
        event.setInitiator(userService.getByIdWithCheck(userId));
        return toEventFullDto(repository.save(event));
    }

    private static void checkEventDate(NewEventDto newEventDto) {
        LocalDateTime dateTime = toLocalDateTime(newEventDto.getEventDate());
        if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + dateTime);
        }
    }
}
