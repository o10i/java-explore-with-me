package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.EndpointHit;
import ru.practicum.model.Hit;

import static ru.practicum.mapper.DateTimeMapper.toLocalDateTime;
import static ru.practicum.mapper.DateTimeMapper.toStringDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HitMapper {
    public static Hit toHit(EndpointHit endpointHitDto) {
        Hit hit = new Hit();
        hit.setApp(endpointHitDto.getApp());
        hit.setUri(endpointHitDto.getUri());
        hit.setIp(endpointHitDto.getIp());
        hit.setTimestamp(toLocalDateTime(endpointHitDto.getTimestamp()));
        return hit;
    }

    public static EndpointHit toEndpointHit(Hit hit) {
        return new EndpointHit(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                toStringDateTime(hit.getTimestamp()));
    }
}
