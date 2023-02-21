package ru.practicum.ewm.APIprivate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.*;
import ru.practicum.ewm.common.enums.Status;
import ru.practicum.ewm.common.exception.BadRequestException;
import ru.practicum.ewm.common.exception.ConflictException;
import ru.practicum.ewm.common.exception.ForbiddenException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.mapper.EventMapper;
import ru.practicum.ewm.common.mapper.RequestMapper;
import ru.practicum.ewm.common.model.Category;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.Request;
import ru.practicum.ewm.common.repository.CategoryRepository;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.repository.RequestRepository;
import ru.practicum.ewm.common.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.common.enums.State.CANCELED;
import static ru.practicum.ewm.common.enums.State.PENDING;
import static ru.practicum.ewm.common.enums.Status.CONFIRMED;
import static ru.practicum.ewm.common.enums.Status.REJECTED;
import static ru.practicum.ewm.common.enums.UserStateAction.CANCEL_REVIEW;
import static ru.practicum.ewm.common.enums.UserStateAction.SEND_TO_REVIEW;
import static ru.practicum.ewm.common.mapper.DateTimeMapper.toLocalDateTime;
import static ru.practicum.ewm.common.mapper.RequestMapper.toParticipationRequestDtoList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivateEventServiceImpl implements PrivateEventService {
    EventRepository repository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    RequestRepository requestRepository;

    @Override
    public List<EventShortDto> getAllByInitiatorId(Long userId, Integer from, Integer size) {
        return EventMapper.toEventShortDtoList(repository.findAllByInitiatorId(userId)
                .stream().skip(from).limit(size).collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public EventFullDto save(Long userId, NewEventDto newEventDto) {
        checkEventDateByInitiator(newEventDto.getEventDate());
        if (newEventDto.getAnnotation() == null) {
            throw new BadRequestException("Field: annotation. Error: must not be blank. Value: null");
        }
        Event event = EventMapper.toEvent(newEventDto);
        event.setCategory(categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", newEventDto.getCategory()))));
        event.setConfirmedRequests(0L);
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId))));
        return EventMapper.toEventFullDto(repository.save(event));
    }

    @Override
    public EventFullDto getByIdAndInitiatorId(Long eventId, Long userId) {
        return EventMapper.toEventFullDto(getByIdAndInitiatorIdWithCheck(eventId, userId));
    }

    @Transactional
    @Override
    public EventFullDto updateByInitiator(Long eventId, Long userId, UpdateEventUserRequest updateEventUserRequest) {
        checkEventDateByInitiator(updateEventUserRequest.getEventDate());

        Event event = getByIdAndInitiatorIdWithCheck(eventId, userId);

        setStateByInitiator(updateEventUserRequest, event);

        updateEventByInitiator(updateEventUserRequest, event);

        return EventMapper.toEventFullDto(repository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByEventIdAndInitiatorId(Long eventId, Long userId) {
        getByIdAndInitiatorIdWithCheck(eventId, userId);
        return toParticipationRequestDtoList(requestRepository.findAllByEventId(eventId));
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequestStatusByInitiator(Long eventId, Long userId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        List<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        String status = eventRequestStatusUpdateRequest.getStatus();

        Event event = getByIdAndInitiatorIdWithCheck(eventId, userId);
        Boolean requestModeration = event.getRequestModeration();
        Long participantLimit = event.getParticipantLimit();
        Long approvedRequests = event.getConfirmedRequests();
        Long availableParticipants = participantLimit - approvedRequests;
        Long potentialParticipants = (long) requestIds.size();

        List<ParticipationRequestDto> confirmedRequests = List.of();
        List<ParticipationRequestDto> rejectedRequests = List.of();

        List<Request> requests = requestIds.stream()
                .map(this::getRequestByIdWithCheck)
                .collect(Collectors.toList());

        if (status.equals(REJECTED.toString())) {
            rejectedRequests = requests.stream()
                    .peek(request -> request.setStatus(REJECTED))
                    .map(requestRepository::save)
                    .map(RequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());
        } else if (status.equals(CONFIRMED.toString())) {
            if (participantLimit != 0 && participantLimit.equals(approvedRequests)) {
                throw new ConflictException("The participant limit has been reached");
            } else if (participantLimit == 0 && !requestModeration) {
                confirmedRequests = requests.stream()
                        .peek(request -> request.setStatus(CONFIRMED))
                        .map(requestRepository::save)
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(approvedRequests + potentialParticipants);
            } else if (potentialParticipants <= availableParticipants) {
                confirmedRequests = requests.stream()
                        .peek(request -> request.setStatus(CONFIRMED))
                        .map(requestRepository::save)
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(approvedRequests + potentialParticipants);
            } else {
                confirmedRequests = requests.stream()
                        .limit(availableParticipants)
                        .peek(request -> request.setStatus(CONFIRMED))
                        .map(requestRepository::save)
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                rejectedRequests = requests.stream()
                        .skip(availableParticipants)
                        .peek(request -> request.setStatus(REJECTED))
                        .map(requestRepository::save)
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(event.getParticipantLimit());
            }
        } else {
            throw new ForbiddenException("Only rejected or confirmed status expected");
        }
        repository.save(event);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private void checkEventDateByInitiator(String eventDate) {
        LocalDateTime dateTime = toLocalDateTime(eventDate);
        if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + dateTime);
        }
    }

    private void updateEventByInitiator(UpdateEventUserRequest updateEventUserRequest, Event event) {
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(getCategoryByIdWithCheck(updateEventUserRequest.getCategory()));
        }
        if (updateEventUserRequest.getDescription() != null) {
            event.setAnnotation(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(toLocalDateTime(updateEventUserRequest.getEventDate()));
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(updateEventUserRequest.getLocation());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
    }

    private void setStateByInitiator(UpdateEventUserRequest updateEventUserRequest, Event event) {
        if (!event.getState().equals(PENDING) && !event.getState().equals(CANCELED)) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }
        if (updateEventUserRequest.getStateAction().equals(CANCEL_REVIEW.toString())) {
            event.setState(CANCELED);
        }
        if (updateEventUserRequest.getStateAction().equals(SEND_TO_REVIEW.toString())) {
            event.setState(PENDING);
        }
    }

    private Category getCategoryByIdWithCheck(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
    }

    private Event getByIdAndInitiatorIdWithCheck(Long eventId, Long initiatorId) {
        return repository.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d and initiatorId=%d was not found", eventId, initiatorId)));
    }

    private Request getRequestByIdWithCheck(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d was not found", requestId)));

        if (!request.getStatus().equals(Status.PENDING)) {
            throw new BadRequestException("Request must have status PENDING");
        }

        return request;
    }
}
