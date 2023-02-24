package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.server.service.StatsService;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStats>> getStats(@RequestParam @NotBlank String start,
                                                    @RequestParam @NotBlank String end,
                                                    @RequestParam List<String> uris,
                                                    @RequestParam(defaultValue = "false") Boolean unique) {
        return ResponseEntity.ok(service.getStats(start, end, uris, unique));
    }

    @PostMapping("/hit")
    public ResponseEntity<EndpointHit> save(@RequestBody EndpointHit endpointHitDto) {
        return new ResponseEntity<>(service.save(endpointHitDto), HttpStatus.CREATED);
    }
}

