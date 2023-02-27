package ru.practicum.ewm.apiPrivate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.apiPrivate.service.PrivateCommentService;
import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.common.dto.NewCommentDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final PrivateCommentService service;

    @GetMapping()
    public ResponseEntity<List<CommentDto>> getAllByAuthorId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getAllByAuthorId(userId));
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<CommentDto> save(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @RequestBody @Valid NewCommentDto newCommentDto) {
        return new ResponseEntity<>(service.save(userId, eventId, newCommentDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> update(@PathVariable Long userId,
                                             @PathVariable Long commentId,
                                             @RequestBody NewCommentDto newCommentDto) {
        return ResponseEntity.ok(service.update(commentId, userId, newCommentDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId,
                                       @PathVariable Long commentId) {
        service.delete(commentId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
