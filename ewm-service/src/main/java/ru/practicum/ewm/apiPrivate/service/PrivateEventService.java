package ru.practicum.ewm.apiPrivate.service;

import ru.practicum.ewm.common.dto.*;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> getAllByInitiatorId(Long userId, Integer from, Integer size);

    EventFullDto save(Long userId, NewEventDto newEventDto);

    EventFullDto getByIdAndInitiatorId(Long eventId, Long userId);

    EventFullDto updateByInitiator(Long eventId, Long userId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getRequestsByEventIdAndInitiatorId(Long eventId, Long userId);

    EventRequestStatusUpdateResult updateRequestStatusByInitiator(Long eventId, Long userId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
