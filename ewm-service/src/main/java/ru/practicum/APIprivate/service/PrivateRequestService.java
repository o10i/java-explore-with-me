package ru.practicum.APIprivate.service;

import ru.practicum.common.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    List<ParticipationRequestDto> getAllByUserId(Long userId);

    ParticipationRequestDto save(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
