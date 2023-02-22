package ru.practicum.ewm.APIpublic.service;

import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getAllByPublicRequest(String text, List<Long> categories, Boolean paid, String rangeStart,
                                              String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                              Integer size, HttpServletRequest request);

    EventFullDto getById(Long id, HttpServletRequest request);
}
