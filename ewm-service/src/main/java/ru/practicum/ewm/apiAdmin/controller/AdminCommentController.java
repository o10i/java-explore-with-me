package ru.practicum.ewm.apiAdmin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.apiAdmin.service.AdminCommentService;
import ru.practicum.ewm.common.dto.CommentDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final AdminCommentService service;

    @GetMapping("{commentId}")
    public ResponseEntity<CommentDto> getById(@PathVariable Long commentId) {
        return ResponseEntity.ok(service.getById(commentId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId) {
        service.delete(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
