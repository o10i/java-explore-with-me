package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.service.StatsService;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    public ResponseEntity<Void> save(@RequestBody HitDto hitDto) {
        service.save(hitDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStats>> getStats(@RequestParam @NotBlank String start,
                                                    @RequestParam @NotBlank String end,
                                                    @RequestParam List<String> uris,
                                                    @RequestParam(defaultValue = "false") Boolean unique) {
        return new ResponseEntity<>(service.getStats(start, end, uris, unique), HttpStatus.OK);
    }
}

