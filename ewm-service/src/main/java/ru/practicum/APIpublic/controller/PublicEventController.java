package ru.practicum.APIpublic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/*import ru.practicum.dto.EventRequestDto;
import ru.practicum.service.EventService;*/

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
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
