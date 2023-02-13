package ru.practicum.APIadmin.service;

import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventService {
    List<EventFullDto> getAllByAdminRequest(List<Long> users, List<String> states, List<Long> categories,
                                            String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
