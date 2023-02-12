package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.EventRequestDto;
import ru.practicum.model.Event;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMapper {
    public static Event toEvent(EventRequestDto eventRequestDto) {
        Event event = new Event();
        event.setName(eventRequestDto.getName());
        return event;
    }
}
