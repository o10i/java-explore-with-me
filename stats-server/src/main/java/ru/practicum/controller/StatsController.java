package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.service.StatsService;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam @NotBlank String start,
                                                    @RequestParam @NotBlank String end,
                                                    @RequestParam List<String> uris,
                                                    @RequestParam(defaultValue = "false") Boolean unique) {
        return ResponseEntity.ok(service.getStats(start, end, uris, unique));
    }

    @PostMapping("/hit")
    public ResponseEntity<Void> save(@RequestBody EndpointHitDto endpointHitDto) {
        service.save(endpointHitDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

