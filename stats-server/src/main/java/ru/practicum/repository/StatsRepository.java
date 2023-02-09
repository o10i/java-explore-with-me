package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Hit;
import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.dto.ViewStats(h.app, h.uri, count(distinct h.ip)) " +
            "from  Hit h " +
            "where h.timestamp between ?1 and ?2 " +
            "and h.uri in (?3) " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<ViewStats> findUniqueViews(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.dto.ViewStats(h.app, h.uri, count(h.ip)) " +
            "from  Hit h " +
            "where h.timestamp between ?1 and ?2 " +
            "and h.uri in (?3) " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<ViewStats> findViews(LocalDateTime start, LocalDateTime end, List<String> uris);
}
