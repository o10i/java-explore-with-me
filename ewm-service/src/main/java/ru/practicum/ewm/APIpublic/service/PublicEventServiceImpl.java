package ru.practicum.ewm.APIpublic.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.EventShortDto;
import ru.practicum.ewm.common.enums.EventSort;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.BadRequestException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.QEvent;
import ru.practicum.ewm.common.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ru.practicum.ewm.common.mapper.DateTimeMapper.toLocalDateTime;
import static ru.practicum.ewm.common.mapper.EventMapper.toEventFullDto;
import static ru.practicum.ewm.common.mapper.EventMapper.toEventShortDtoList;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository repository;

    @Override
    public List<EventShortDto> getAllByPublicRequest(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {

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
/*        if (onlyAvailable == Boolean.TRUE) {
            conditions.add(event.participantLimit.gt(paid));
        }*/

        BooleanExpression expression = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        List<Event> events = StreamSupport.stream(repository.findAll(expression, sortBy).spliterator(), false)
                .collect(Collectors.toList());

        return toEventShortDtoList(events).stream().skip(from).limit(size).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getById(Long id) {
        Event event = repository.findByIdAndState(id, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(String.format("Published event with id=%d was not found", id)));
        return toEventFullDto(event);
    }
}
