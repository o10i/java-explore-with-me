package ru.practicum.APIprivate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.APIprivate.service.PrivateEventService;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventShortDto;
import ru.practicum.common.dto.NewEventDto;
import ru.practicum.common.dto.UpdateEventUserRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final PrivateEventService service;

    @GetMapping("")
    public ResponseEntity<List<EventShortDto>> getAllByInitiatorId(@PathVariable Long userId,
                                                                   @RequestParam(defaultValue = "0") Integer from,
                                                                   @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getAllByInitiatorId(userId, from, size));
    }

    @PostMapping()
    public ResponseEntity<EventFullDto> save(@PathVariable Long userId,
                                             @RequestBody @Valid NewEventDto newEventDto) {
        return new ResponseEntity<>(service.save(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getByIdAndInitiatorId(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getByIdAndInitiatorId(eventId, userId), HttpStatus.OK);
    }

    @PatchMapping("{eventId}")
    public ResponseEntity<EventFullDto> updateByInitiator(@PathVariable Long userId,
                                                          @PathVariable Long eventId,
                                                          @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return ResponseEntity.ok(service.updateByInitiator(eventId, userId, updateEventUserRequest));
    }
}
