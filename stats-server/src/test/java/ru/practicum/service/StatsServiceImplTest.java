package ru.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {
    @InjectMocks
    private StatsServiceImpl service;
    @Mock
    private StatsRepository repository;

    @Test
    void save_thenHitSaved() {
        when(repository.save(any())).thenReturn(any());

        service.save(new HitDto("ewm-main-service", "/events/1", "73.80.0.87", "2023-02-09 13:50:47"));

        verify(repository, times(1)).save(any());
    }

    @Test
    void getStats_whenUniqueFalse_thenFoundViewStatsListReturned() {
        List<ViewStats> viewStatsList = List.of(new ViewStats("ewm-main-service", "/events/1", 1L));
        when(repository.findViewStats(any(), any(), any())).thenReturn(viewStatsList);

        List<ViewStats> actualViewStatsList = service.getStats("2023-02-09 13:50:47", "2023-02-09 13:50:47", List.of(), Boolean.FALSE);

        assertEquals(viewStatsList, actualViewStatsList);
    }

    @Test
    void getStats_whenUniqueTrue_thenFoundUniqueViewStatsListReturned() {
        List<ViewStats> viewStatsList = List.of(new ViewStats("ewm-main-service", "/events/1", 1L));
        when(repository.findUniqueViewStats(any(), any(), any())).thenReturn(viewStatsList);

        List<ViewStats> actualViewStatsList = service.getStats("2023-02-09 13:50:47", "2023-02-09 13:50:47", List.of(), Boolean.TRUE);

        assertEquals(viewStatsList, actualViewStatsList);
    }
}