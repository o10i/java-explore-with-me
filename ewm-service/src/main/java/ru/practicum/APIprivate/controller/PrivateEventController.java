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
@RequestMapping("/users")
public class PrivateEventController {
    private final PrivateEventService service;

    @GetMapping("/{initiatorId}/events")
    public ResponseEntity<List<EventShortDto>> getAllByInitiatorId(@PathVariable Long initiatorId,
                                                                   @RequestParam(defaultValue = "0") Integer from,
                                                                   @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getAllByInitiatorId(initiatorId, from, size));
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> save(@PathVariable Long userId,
                                             @RequestBody @Valid NewEventDto newEventDto) {
        return new ResponseEntity<>(service.save(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping("/{initiatorId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getByIdAndInitiatorId(@PathVariable Long initiatorId,
                                                              @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getByIdAndInitiatorId(eventId, initiatorId), HttpStatus.OK);
    }

    @PatchMapping("/{initiatorId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateByInitiator(@PathVariable Long initiatorId,
                                                          @PathVariable Long eventId,
                                                          @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return ResponseEntity.ok(service.updateByInitiator(eventId, initiatorId, updateEventUserRequest));
    }
}
