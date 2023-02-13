package ru.practicum.APIadmin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.APIadmin.service.AdminEventService;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.UpdateEventAdminRequest;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final AdminEventService service;

    @GetMapping()
    public ResponseEntity<List<EventFullDto>> getAllByAdminRequest(@RequestParam(required = false) List<Long> users,
                                                                   @RequestParam(required = false) List<String> states,
                                                                   @RequestParam(required = false) List<Long> categories,
                                                                   @RequestParam(required = false) String rangeStart,
                                                                   @RequestParam(required = false) String rangeEnd,
                                                                   @RequestParam(defaultValue = "0") Integer from,
                                                                   @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getAllByAdminRequest(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> update(@PathVariable Long eventId,
                                               @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return ResponseEntity.ok(service.updateByAdmin(eventId, updateEventAdminRequest));
    }
}
