package ru.practicum.common.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.common.dto.ParticipationRequestDto;
import ru.practicum.common.model.Event;
import ru.practicum.common.model.Request;
import ru.practicum.common.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.common.enums.Status.CONFIRMED;
import static ru.practicum.common.enums.Status.PENDING;
import static ru.practicum.common.mapper.DateTimeMapper.toStringDateTime;


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
