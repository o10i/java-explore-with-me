package ru.practicum.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static ru.practicum.mapper.DateTimeMapper.toStringDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiError {
    String status;
    String reason;
    String message;
    String timestamp;

    public ApiError(String status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = toStringDateTime(LocalDateTime.now());
    }
}
