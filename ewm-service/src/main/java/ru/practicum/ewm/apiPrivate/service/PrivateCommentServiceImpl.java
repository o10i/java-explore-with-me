package ru.practicum.ewm.apiPrivate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.common.dto.NewCommentDto;
import ru.practicum.ewm.common.enums.State;
import ru.practicum.ewm.common.exception.BadRequestException;
import ru.practicum.ewm.common.exception.ForbiddenException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.model.Comment;
import ru.practicum.ewm.common.model.Event;
import ru.practicum.ewm.common.model.User;
import ru.practicum.ewm.common.repository.CommentRepository;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.common.mapper.CommentMapper.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivateCommentServiceImpl implements PrivateCommentService {
    CommentRepository repository;
    UserRepository userRepository;
    EventRepository eventRepository;

    @Override
    public List<CommentDto> getAllByAuthorId(Long userId) {
        getUserByIdWithCheck(userId);
        return toCommentDtoList(repository.findAllByAuthorId(userId));
    }

    @Transactional
    @Override
    public CommentDto save(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User author = getUserByIdWithCheck(userId);
        Event event = getEventByIdWithCheck(eventId);

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new BadRequestException("Event must be published");
        }

        Comment comment = toComment(newCommentDto);
        comment.setEvent(event);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        return toCommentDto(repository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(Long commentId, Long userId, NewCommentDto newCommentDto) {
        Comment comment = getByIdAndAuthorIdWithCheck(commentId, userId);

        if (comment.getCreated().plusDays(1).isBefore(LocalDateTime.now())) {
            throw new ForbiddenException("The comment update time has expired");
        }

        comment.setText(newCommentDto.getText());

        return toCommentDto(repository.save(comment));
    }

    @Transactional
    @Override
    public void delete(Long commentId, Long userId) {
        getByIdAndAuthorIdWithCheck(commentId, userId);
        repository.deleteById(commentId);
    }

    private Comment getByIdAndAuthorIdWithCheck(Long commentId, Long userId) {
        return repository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d and authorId=%d was not found", commentId, userId)));
    }

    private User getUserByIdWithCheck(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private Event getEventByIdWithCheck(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }
}
