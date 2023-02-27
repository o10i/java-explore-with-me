package ru.practicum.ewm.apiPrivate.service;

import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.common.dto.NewCommentDto;

import java.util.List;

public interface PrivateCommentService {
    List<CommentDto> getAllByAuthorId(Long userId);

    CommentDto save(Long userId, Long eventId, NewCommentDto newCommentDto);

    void delete(Long commentId, Long userId);

    CommentDto update(Long commentId, Long userId, NewCommentDto newCommentDto);
}
