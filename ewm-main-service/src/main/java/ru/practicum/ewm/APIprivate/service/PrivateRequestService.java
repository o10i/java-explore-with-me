package ru.practicum.ewm.APIprivate.service;

import ru.practicum.ewm.common.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    List<ParticipationRequestDto> getAllByUserId(Long userId);

    ParticipationRequestDto sendRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long eventId);
}
