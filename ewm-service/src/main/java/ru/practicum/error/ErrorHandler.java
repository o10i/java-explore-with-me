package ru.practicum.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                "NOT_FOUND",
                "The required object was not found.",
                e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                "BAD_REQUEST",
                "Incorrectly made request.",
                e.getMessage());
    }
    /*
        @ExceptionHandler
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public ErrorResponse handleForbiddenException(final ForbiddenException e) {
            log.error(e.getLocalizedMessage(), e.getMessage());
            return new ErrorResponse(e.getMessage());
        }
*/
}
