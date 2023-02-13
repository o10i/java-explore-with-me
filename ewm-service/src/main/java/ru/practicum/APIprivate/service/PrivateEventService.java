package ru.practicum.APIprivate.service;

import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventShortDto;
import ru.practicum.common.dto.NewEventDto;
import ru.practicum.common.dto.UpdateEventUserRequest;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> getAllByInitiatorId(Long initiatorId, Integer from, Integer size);

    EventFullDto save(Long userId, NewEventDto newEventDto);

    EventFullDto getByIdAndInitiatorId(Long eventId, Long initiatorId);

    EventFullDto updateByInitiator(Long eventId, Long initiatorId, UpdateEventUserRequest updateEventUserRequest);
}
