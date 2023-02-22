package ru.practicum.ewm.APIpublic.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.client.HitClient;
import ru.practicum.dto.ViewStats;
import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.EventShortDto;
import ru.practicum.ewm.common.enums.EventSort;
import ru.practicum.ewm.common.exception.BadRequestException;
import ru.practicum.ewm.common.exception.ForbiddenException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.QEvent;
import ru.practicum.ewm.common.repository.EventRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ru.practicum.ewm.common.enums.State.PUBLISHED;
import static ru.practicum.ewm.common.mapper.DateTimeMapper.toLocalDateTime;
import static ru.practicum.ewm.common.mapper.DateTimeMapper.toStringDateTime;
import static ru.practicum.ewm.common.mapper.EventMapper.toEventFullDto;
import static ru.practicum.ewm.common.mapper.EventMapper.toEventShortDtoList;
import static ru.practicum.ewm.common.mapper.HitMapper.toEndpointHit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicEventServiceImpl implements PublicEventService {
    EventRepository repository;
    HitClient hitClient;

    @Override
    public List<EventShortDto> getAllByPublicRequest(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                     String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                                     Integer size, HttpServletRequest request) {
        hitClient.save(toEndpointHit(request));

        Sort sortBy = Sort.unsorted();
        if (sort != null) {
            if (sort.toUpperCase().equals(EventSort.EVENT_DATE.toString())) {
                sortBy = Sort.by("eventDate");
            } else if (sort.toUpperCase().equals(EventSort.VIEWS.toString())) {
                sortBy = Sort.by("views");
            } else {
                throw new BadRequestException("Field: sort. Error: must be EVENT_DATE or VIEWS. Value: " + sort);
            }
        }

        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        if (text != null && !text.isEmpty()) {
            conditions.add(event.annotation.toLowerCase().like('%' + text.toLowerCase() + '%')
                    .or(event.description.toLowerCase().like('%' + text.toLowerCase() + '%')));
        }
        if (categories != null && !categories.isEmpty()) {
            conditions.add(event.category.id.in(categories));
        }
        if (paid != null) {
            conditions.add(event.paid.eq(paid));
        }
        if (rangeStart != null && !rangeStart.isEmpty()) {
            conditions.add(event.eventDate.after(toLocalDateTime(rangeStart)));
        }
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            conditions.add(event.eventDate.before(toLocalDateTime(rangeEnd)));
        }
        if ((rangeStart == null || rangeStart.isEmpty()) && (rangeEnd == null || rangeEnd.isEmpty())) {
            conditions.add(event.eventDate.after(LocalDateTime.now()));
        }
        if (onlyAvailable == Boolean.TRUE) {
            conditions.add(event.participantLimit.gt(event.confirmedRequests));
        }

        BooleanExpression expression = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        List<Event> events = StreamSupport.stream(repository.findAll(expression, sortBy).spliterator(), false)
                .filter(event1 -> event1.getState().equals(PUBLISHED))
                .skip(from).limit(size)
                .collect(Collectors.toList());

        HashMap<Long, Long> eventsViews = getEventsViews(events);

        for (Event e : events) {
            e.setViews(eventsViews.get(e.getId()));
        }

        return toEventShortDtoList(events);
    }

    @Override
    public EventFullDto getById(Long id, HttpServletRequest request) {
        hitClient.save(toEndpointHit(request));

        Event event = getByIdWithCheck(id);

        if (!event.getState().equals(PUBLISHED)) {
            throw new ForbiddenException(String.format("Event with id=%d is not published", id));
        }

        event.setViews(getEventViews(event));

        return toEventFullDto(event);
    }

    private Event getByIdWithCheck(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", id)));
    }

    private Long getEventViews(Event event) {
        List<ViewStats> viewStats = new ObjectMapper().convertValue(hitClient.getStats(
                toStringDateTime(event.getCreatedOn()),
                toStringDateTime(LocalDateTime.now()),
                List.of("/events/" + event.getId()),
                false).getBody(), new TypeReference<>() {
        });
        return viewStats.isEmpty() ? 0 : viewStats.get(0).getHits();
    }

    private HashMap<Long, Long> getEventsViews(List<Event> events) {
        HashMap<Long, Long> eventsViews = new HashMap<>();
        List<String> uris = new ArrayList<>();

        List<Long> eventsId = events.stream().map(Event::getId).collect(Collectors.toList());
        for (Long eventId : eventsId) {
            eventsViews.put(eventId, 0L);
            uris.add("/events/" + eventId);
        }

        List<ViewStats> viewStats = new ObjectMapper().convertValue(hitClient.getStats(
                "2000-01-01 00:00:00",
                toStringDateTime(LocalDateTime.now()),
                uris,
                false).getBody(), new TypeReference<>() {
        });

        for (Long eventId : eventsId) {
            for (ViewStats viewStat : viewStats) {
                if (viewStat.getUri().equals("/events/" + eventId)) {
                    eventsViews.put(eventId, viewStat.getHits());
                }
            }
        }

        return eventsViews;
    }
}
