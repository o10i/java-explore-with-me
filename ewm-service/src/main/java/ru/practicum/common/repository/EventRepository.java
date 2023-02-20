package ru.practicum.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.common.enums.State;
import ru.practicum.common.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event>,
        QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiatorId(Long initiatorId);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);
    Optional<Event> findByIdAndState(Long eventId, State state);
    List<Event> findAllByIdIn(Set<Long> events);
}
