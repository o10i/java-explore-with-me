package ru.practicum.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.server.model.Hit;
import ru.practicum.server.repository.HitRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.practicum.server.mapper.DateTimeMapper.toLocalDateTime;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {
    @InjectMocks
    private StatsServiceImpl service;
    @Mock
    private HitRepository repository;

    @Test
    void save_thenHitSaved() {
        when(repository.save(any())).thenReturn(new Hit(
                1L,
                "ewm-service",
                "/events/1",
                "73.80.0.87",
                toLocalDateTime("2023-02-09 13:50:47")));

        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp("ewm-service");
        endpointHit.setUri("/events/1");
        endpointHit.setIp("73.80.0.87");
        endpointHit.setTimestamp("2023-02-09 13:50:47");
        service.save(endpointHit);

        verify(repository, times(1)).save(any());
    }

    @Test
    void getStats_whenUniqueFalse_thenFoundViewStatsListReturned() {
        List<ViewStats> viewStatsList = List.of(new ViewStats("ewm-service", "/events/1", 1L));
        when(repository.findViewStats(any(), any(), any())).thenReturn(viewStatsList);

        List<ViewStats> actualViewStatsList = service.getStats("2023-02-09 13:50:47", "2023-02-09 13:50:47", List.of(), Boolean.FALSE);

        assertEquals(viewStatsList, actualViewStatsList);
    }

    @Test
    void getStats_whenUniqueTrue_thenFoundUniqueViewStatsListReturned() {
        List<ViewStats> viewStatsList = List.of(new ViewStats("ewm-service", "/events/1", 1L));
        when(repository.findUniqueViewStats(any(), any(), any())).thenReturn(viewStatsList);

        List<ViewStats> actualViewStatsList = service.getStats("2023-02-09 13:50:47", "2023-02-09 13:50:47", List.of(), Boolean.TRUE);

        assertEquals(viewStatsList, actualViewStatsList);
    }
}