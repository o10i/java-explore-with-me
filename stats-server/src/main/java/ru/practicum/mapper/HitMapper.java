package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.EndpointHit;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HitMapper {
    public static Hit toHit(EndpointHit endpointHitDto) {
        Hit hit = new Hit();
        hit.setApp(endpointHitDto.getApp());
        hit.setUri(endpointHitDto.getUri());
        hit.setIp(endpointHitDto.getIp());
        hit.setTimestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return hit;
    }

    public static EndpointHit toEndpointHit(Hit hit) {
        return new EndpointHit(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
