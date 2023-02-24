package ru.practicum.ewm.common.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.common.dto.ParticipationRequestDto;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.common.enums.Status.CONFIRMED;
import static ru.practicum.ewm.common.enums.Status.PENDING;
import static ru.practicum.ewm.common.mapper.DateTimeMapper.toStringDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestMapper {

    public static Request toRequest(Event event, User requester) {
        Request request = new Request();
        request.setEvent(event);
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());
        request.setStatus(event.getRequestModeration() ? PENDING : CONFIRMED);
        return request;
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                toStringDateTime(request.getCreated()),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus().toString()
        );
    }

    public static List<ParticipationRequestDto> toParticipationRequestDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }
}
