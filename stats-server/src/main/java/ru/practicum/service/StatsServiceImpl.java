package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHit;
import ru.practicum.mapper.HitMapper;
import ru.practicum.dto.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.mapper.HitMapper.toEndpointHit;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    private static LocalDateTime getDateTime(String dateTime) {
        dateTime = URLDecoder.decode(dateTime, StandardCharsets.UTF_8);
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public EndpointHit save(EndpointHit endpointHitDto) {
        return toEndpointHit(repository.save(HitMapper.toHit(endpointHitDto)));
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        if (unique) {
            return repository.findUniqueViewStats(getDateTime(start), getDateTime(end), uris);
        } else {
            return repository.findViewStats(getDateTime(start), getDateTime(end), uris);
        }
    }
}
