package ru.practicum.ewm.APIadmin.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.Location;
import ru.practicum.ewm.common.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.common.exception.ForbiddenException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.mapper.EventMapper;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.QEvent;
import ru.practicum.ewm.common.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ru.practicum.ewm.common.enums.AdminStateAction.PUBLISH_EVENT;
import static ru.practicum.ewm.common.enums.AdminStateAction.REJECT_EVENT;
import static ru.practicum.ewm.common.enums.State.CANCELED;
import static ru.practicum.ewm.common.enums.State.PUBLISHED;
import static ru.practicum.ewm.common.mapper.DateTimeMapper.toLocalDateTime;
import static ru.practicum.ewm.common.mapper.EventMapper.toEventFullDtoList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository repository;
    private final AdminCategoryServiceImpl categoryService;

    @Override
    public List<EventFullDto> getAllByAdminRequest(List<Long> users, List<String> states, List<Long> categories,
                                                   String rangeStart, String rangeEnd, Integer from, Integer size) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        if (users != null && !users.isEmpty()) {
            conditions.add(event.initiator.id.in(users));
        }
        if (states != null && !states.isEmpty()) {
            conditions.add(event.state.in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            conditions.add(event.category.id.in(categories));
        }
        if (rangeStart != null && !rangeStart.isEmpty()) {
            conditions.add(event.eventDate.after(toLocalDateTime(rangeStart)));
        }
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            conditions.add(event.eventDate.before(toLocalDateTime(rangeEnd)));
        }

        List<Event> events;
        if (conditions.isEmpty()) {
            events = repository.findAll();
        } else {
            BooleanExpression expression = conditions.stream()
                    .reduce(BooleanExpression::and)
                    .get();

            events = StreamSupport.stream(repository.findAll(expression).spliterator(), false)
                    .collect(Collectors.toList());
        }

        return toEventFullDtoList(events).stream().skip(from).limit(size).collect(Collectors.toList());
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
        LocalDateTime eventDate = event.getEventDate();
        LocalDateTime publishedOn = event.getPublishedOn();

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
        Long participantLimit = updateEventAdminRequest.getParticipantLimit();
        Boolean requestModeration = updateEventAdminRequest.getRequestModeration();
        String title = updateEventAdminRequest.getTitle();
        String stateAction = updateEventAdminRequest.getStateAction();

        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        if (category != null) {
            event.setCategory(categoryService.getByIdWithCheck(category));
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
        if (stateAction.equals(PUBLISH_EVENT.toString())) {
            event.setState(PUBLISHED);
        }
        if (stateAction.equals(REJECT_EVENT.toString())) {
            event.setState(CANCELED);
        }
    }
}
