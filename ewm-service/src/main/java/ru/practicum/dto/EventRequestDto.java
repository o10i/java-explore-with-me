package ru.practicum.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestDto {
    String annotation;
    Long category;
    String description;
    String eventDate;
    LocationRequestDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String title;
}
