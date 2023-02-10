package ru.practicum.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HitMapper {
    public static Hit toHit(HitDto hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return hit;
    }
}
