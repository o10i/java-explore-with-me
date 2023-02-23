package ru.practicum.ewm.apiPublic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.apiPublic.service.PublicEventService;
import ru.practicum.ewm.common.dto.EventFullDto;
import ru.practicum.ewm.common.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final PublicEventService service;

    @GetMapping()
    public ResponseEntity<List<EventShortDto>> getAllByPublicRequest(@RequestParam(required = false) String text,
                                                                     @RequestParam(required = false) List<Long> categories,
                                                                     @RequestParam(required = false) Boolean paid,
                                                                     @RequestParam(required = false) String rangeStart,
                                                                     @RequestParam(required = false) String rangeEnd,
                                                                     @RequestParam(defaultValue = "FALSE") Boolean onlyAvailable,
                                                                     @RequestParam(required = false) String sort,
                                                                     @RequestParam(defaultValue = "0") Integer from,
                                                                     @RequestParam(defaultValue = "10") Integer size,
                                                                     HttpServletRequest request) {
        return ResponseEntity.ok(service.getAllByPublicRequest(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getById(@PathVariable Long id,
                                                HttpServletRequest request) {
        return ResponseEntity.ok(service.getById(id, request));
    }
}
