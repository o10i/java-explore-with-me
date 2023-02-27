package ru.practicum.ewm.apiAdmin.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.model.Comment;
import ru.practicum.ewm.common.repository.CommentRepository;

import static ru.practicum.ewm.common.mapper.CommentMapper.toCommentDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCommentServiceImpl implements AdminCommentService {
    CommentRepository repository;

    @Override
    public CommentDto getById(Long commentId) {
        return toCommentDto(getByIdWithCheck(commentId));
    }

    @Transactional
    @Override
    public void delete(Long commentId) {
        getByIdWithCheck(commentId);
        repository.deleteById(commentId);
    }

    private Comment getByIdWithCheck(Long commentId) {
        return repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d was not found", commentId)));
    }
}
