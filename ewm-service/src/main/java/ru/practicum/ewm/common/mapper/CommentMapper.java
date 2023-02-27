package ru.practicum.ewm.common.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.common.dto.CommentDto;
import ru.practicum.ewm.common.dto.NewCommentDto;
import ru.practicum.ewm.common.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.common.mapper.DateTimeMapper.toStringDateTime;
import static ru.practicum.ewm.common.mapper.EventMapper.toEventShortDto;
import static ru.practicum.ewm.common.mapper.UserMapper.toUserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {
    public static Comment toComment(NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        return comment;
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                toEventShortDto(comment.getEvent()),
                toUserDto(comment.getAuthor()),
                toStringDateTime(comment.getCreated())
        );
    }

    public static List<CommentDto> toCommentDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
