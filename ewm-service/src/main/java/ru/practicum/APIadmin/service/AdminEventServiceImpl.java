package ru.practicum.APIadmin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.Location;
import ru.practicum.common.dto.UpdateEventAdminRequest;
import ru.practicum.common.exception.ForbiddenException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.mapper.DateTimeMapper;
import ru.practicum.common.mapper.EventMapper;
import ru.practicum.common.model.Event;
import ru.practicum.common.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository repository;
    private final AdminCategoryServiceImpl categoryService;

    @Override
    public List<EventFullDto> getAllByAdminRequest(List<Long> users, List<String> states, List<Long> categories,
                                                   String rangeStart, String rangeEnd, Integer from, Integer size) {
        return EventMapper.toEventFullDtoList(repository.findAllByAdminRequest(users, states, categories, rangeStart, rangeEnd)
                .stream().skip(from).limit(size).collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = getByIdWithCheck(eventId);
        checkEventDateByAdmin(event);
        updateEventByAdmin(updateEventAdminRequest, event);
        return EventMapper.toEventFullDto(repository.save(event));
    }

    private Event getByIdWithCheck(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    private void checkEventDateByAdmin(Event event) {
        LocalDateTime eventDate = DateTimeMapper.toLocalDateTime(event.getEventDate());
        LocalDateTime publishedOn = DateTimeMapper.toLocalDateTime(event.getPublishedOn());

        if (eventDate.isBefore(publishedOn.plusHours(1))) {
            throw new ForbiddenException("Field: eventDate. Error: должно содержать дату не ранее чем за час от даты публикации. Value: " + eventDate);
        }
    }

    private void updateEventByAdmin(UpdateEventAdminRequest updateEventAdminRequest, Event event) {
        String annotation = updateEventAdminRequest.getAnnotation();
        Long category = updateEventAdminRequest.getCategory();
        String description = updateEventAdminRequest.getDescription();
        String eventDate = updateEventAdminRequest.getEventDate();
        Location location = updateEventAdminRequest.getLocation();
        Boolean paid = updateEventAdminRequest.getPaid();
        Integer participantLimit = updateEventAdminRequest.getParticipantLimit();
        Boolean requestModeration = updateEventAdminRequest.getRequestModeration();
        String title = updateEventAdminRequest.getTitle();

        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        if (category != null) {
            event.setCategory(categoryService.getByIdWithCheck(category));
        }
        if (description != null) {
            event.setAnnotation(description);
        }
        event.setEventDate(eventDate);
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
}
