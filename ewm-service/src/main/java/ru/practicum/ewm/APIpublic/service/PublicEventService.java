package ru.practicum.ewm.APIpublic.service;

import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.EventShortDto;

import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getAllByPublicRequest(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto getById(Long id);
}
