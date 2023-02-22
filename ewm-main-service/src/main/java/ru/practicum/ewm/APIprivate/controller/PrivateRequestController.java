package ru.practicum.ewm.APIprivate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.APIprivate.service.PrivateRequestService;
import ru.practicum.ewm.common.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {
    private final PrivateRequestService service;

    @GetMapping()
    public ResponseEntity<List<ParticipationRequestDto>> getAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getAllByUserId(userId));
    }

    @PostMapping()
    public ResponseEntity<ParticipationRequestDto> sendRequest(@PathVariable Long userId,
                                                               @RequestParam Long eventId) {
        return new ResponseEntity<>(service.sendRequest(userId, eventId), HttpStatus.CREATED);
    }


    @PatchMapping("/{eventId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable Long userId,
                                                                 @PathVariable Long eventId) {
        return ResponseEntity.ok(service.cancelRequest(userId, eventId));
    }

}
