package ru.practicum.ewm.apiPublic.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.common.repository.CommentRepository;

import java.util.List;

import static ru.practicum.ewm.common.mapper.CommentMapper.toCommentDtoList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCommentServiceImpl implements PublicCommentService {
    CommentRepository repository;

    @Override
    public List<CommentDto> getAllByEventId(Long eventId) {
        return toCommentDtoList(repository.findAllByEventId(eventId));
    }
}
