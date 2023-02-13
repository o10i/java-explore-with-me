package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.HitRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.mapper.DateTimeMapper.toLocalDateTime;
import static ru.practicum.mapper.HitMapper.toEndpointHit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitServiceImpl implements HitService {
    private final HitRepository repository;

    private static LocalDateTime getDateTime(String dateTime) {
        dateTime = URLDecoder.decode(dateTime, StandardCharsets.UTF_8);
        return toLocalDateTime(dateTime);
    }

    @Transactional
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
