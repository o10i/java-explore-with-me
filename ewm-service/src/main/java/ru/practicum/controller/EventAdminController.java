package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.EventService;
/*import ru.practicum.dto.EventRequestDto;
import ru.practicum.service.EventService;*/

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {
//    private final EventService service;

/*    @GetMapping()
    public ResponseEntity<Object> getAll(@RequestParam(required = false) List<Long> ids,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(service.getAll(ids, from, size), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody EventRequestDto eventRequestDto) {
        return new ResponseEntity<>(service.save(eventRequestDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable Long eventId) {
        service.delete(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }*/
}
