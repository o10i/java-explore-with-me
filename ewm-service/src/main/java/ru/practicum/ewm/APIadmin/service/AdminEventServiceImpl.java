package ru.practicum.ewm.APIadmin.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.ConflictException;
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
import static ru.practicum.ewm.common.enums.State.*;
import static ru.practicum.ewm.common.mapper.DateTimeMapper.toLocalDateTime;
import static ru.practicum.ewm.common.mapper.EventMapper.toEventFullDtoList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository repository;
    private final AdminCategoryServiceImpl categoryService;

    @Override
    public List<EventFullDto> getAllByAdminRequest(List<Long> users, List<State> states, List<Long> categories,
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

        setStateByAdmin(eventId, updateEventAdminRequest, event);

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

    private void setStateByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest, Event event) {
        if (updateEventAdminRequest.getStateAction().equals(PUBLISH_EVENT.toString())) {
            if (!event.getState().equals(PENDING)) {
                throw new ConflictException(String.format("Event with id=%d is not PENDING", eventId));
            }
            event.setState(PUBLISHED);
        }
        if (updateEventAdminRequest.getStateAction().equals(REJECT_EVENT.toString())) {
            if (event.getState().equals(PUBLISHED)) {
                throw new ConflictException(String.format("Event with id=%d is PUBLISHED", eventId));
            }
            event.setState(CANCELED);
        }
    }

    private void updateEventByAdmin(UpdateEventAdminRequest updateEventAdminRequest, Event event) {
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryService.getByIdWithCheck(updateEventAdminRequest.getCategory()));
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setAnnotation(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(toLocalDateTime(updateEventAdminRequest.getEventDate()));
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(updateEventAdminRequest.getLocation());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
    }
}
