package ru.practicum.APIprivate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.dto.*;
import ru.practicum.common.enums.UserStateAction;
import ru.practicum.common.exception.ForbiddenException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.mapper.EventMapper;
import ru.practicum.common.model.Event;
import ru.practicum.common.repository.CategoryRepository;
import ru.practicum.common.repository.EventRepository;
import ru.practicum.common.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.common.mapper.DateTimeMapper.toLocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository repository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<EventShortDto> getAllByInitiatorId(Long userId, Integer from, Integer size) {
        return EventMapper.toEventShortDtoList(repository.findAllByInitiatorId(userId)
                .stream().skip(from).limit(size).collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public EventFullDto save(Long userId, NewEventDto newEventDto) {
        checkEventDateByInitiator(newEventDto.getEventDate());
        Event event = EventMapper.toEvent(newEventDto);
        event.setCategory(categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", newEventDto.getCategory()))));
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
        checkStateActionByInitiator(updateEventUserRequest.getStateAction());
        checkEventDateByInitiator(updateEventUserRequest.getEventDate());

        Event event = getByIdAndInitiatorIdWithCheck(eventId, userId);
        updateEventByInitiator(updateEventUserRequest, event);
        return EventMapper.toEventFullDto(repository.save(event));
    }

    private void checkEventDateByInitiator(String eventDate) {
        LocalDateTime dateTime = toLocalDateTime(eventDate);
        if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + dateTime);
        }
    }

    private void checkStateActionByInitiator(String stateAction) {
        if (!stateAction.equals(UserStateAction.SEND_TO_REVIEW.toString()) && !stateAction.equals(UserStateAction.CANCEL_REVIEW.toString())) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }
    }

    private void updateEventByInitiator(UpdateEventUserRequest updateEventUserRequest, Event event) {
        String annotation = updateEventUserRequest.getAnnotation();
        Long category = updateEventUserRequest.getCategory();
        String description = updateEventUserRequest.getDescription();
        String eventDate = updateEventUserRequest.getEventDate();
        Location location = updateEventUserRequest.getLocation();
        Boolean paid = updateEventUserRequest.getPaid();
        Long participantLimit = updateEventUserRequest.getParticipantLimit();
        Boolean requestModeration = updateEventUserRequest.getRequestModeration();
        String title = updateEventUserRequest.getTitle();

        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        if (category != null) {
            event.setCategory(categoryRepository.findById(category)
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", category))));
        }
        if (description != null) {
            event.setAnnotation(description);
        }
        event.setEventDate(toLocalDateTime(eventDate));
        if (location != null) {
            event.setLocation(location);
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        if (title != null) {
            event.setTitle(title);
        }
    }

    private Event getByIdAndInitiatorIdWithCheck(Long eventId, Long initiatorId) {
        return repository.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d and initiatorId=%d was not found", eventId, initiatorId)));
    }
}
