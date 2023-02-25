package ru.practicum.ewm.apiAdmin.service;

import ru.practicum.ewm.common.dto.CommentDto;

public interface AdminCommentService {
    CommentDto getById(Long commentId);

    void delete(Long commentId);
}
