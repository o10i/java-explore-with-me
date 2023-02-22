package ru.practicum.ewm.APIprivate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.ParticipationRequestDto;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.ConflictException;
import ru.practicum.ewm.common.exception.ForbiddenException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.model.User;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.repository.RequestRepository;
import ru.practicum.ewm.common.repository.UserRepository;

import java.util.List;

import static ru.practicum.ewm.common.enums.Status.CONFIRMED;
import static ru.practicum.ewm.common.enums.Status.REJECTED;
import static ru.practicum.ewm.common.mapper.RequestMapper.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivateRequestServiceImpl implements PrivateRequestService {
    RequestRepository repository;
    EventRepository eventRepository;
    UserRepository userRepository;

    @Override
    public List<ParticipationRequestDto> getAllByUserId(Long userId) {
        getUserByIdWithCheck(userId);
        return toParticipationRequestDtoList(repository.findAllByRequesterId(userId));
    }

    @Transactional
    @Override
    public ParticipationRequestDto sendRequest(Long userId, Long eventId) {
        checkRequestExist(userId, eventId);

        Event event = getEventByIdWithCheck(eventId);

        User requester = getUserByIdWithCheck(userId);

        //event.setConfirmedRequests((long) repository.findAllByEventIdAndStatus(eventId, CONFIRMED).size());

        checkConflicts(userId, eventId, event);

        return toParticipationRequestDto(repository.save(toRequest(event, requester)));
    }

    private void checkRequestExist(Long userId, Long eventId) {
        if (repository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ForbiddenException(String.format("Request with requesterId=%d and eventId=%d already exist", userId, eventId));
        }
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long eventId) {
        Request request = getRequestsByIdAndRequesterIdWithCheck(userId, eventId);
        request.setStatus(REJECTED);
        return toParticipationRequestDto(repository.save(request));
    }

    private void checkConflicts(Long userId, Long eventId, Event event) {
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException(String.format("User with id=%d must not be equal to initiator", userId));
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException(String.format("Event with id=%d is not published", eventId));
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ConflictException(String.format("Event with id=%d has reached participant limit", eventId));
        }
    }

    private Event getEventByIdWithCheck(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    private User getUserByIdWithCheck(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private Request getRequestsByIdAndRequesterIdWithCheck(Long userId, Long eventId) {
        return repository.findByEventIdAndRequesterId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d and requesterId=%d was not found", eventId, userId)));
    }
}
