package ru.practicum.ewm.apiPublic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.apiPublic.service.PublicCommentService;
import ru.practicum.ewm.common.dto.CommentDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class PublicCommentController {
    private final PublicCommentService service;

    @GetMapping("{eventId}")
    public ResponseEntity<List<CommentDto>> getAllByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(service.getAllByEventId(eventId));
    }
}
