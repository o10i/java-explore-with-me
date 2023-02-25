package ru.practicum.ewm.apiPublic.service;

import ru.practicum.ewm.common.dto.CommentDto;

import java.util.List;

public interface PublicCommentService {
    List<CommentDto> getAllByEventId(Long eventId);
}
