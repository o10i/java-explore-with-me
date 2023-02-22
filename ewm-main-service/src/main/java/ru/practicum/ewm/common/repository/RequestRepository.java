package ru.practicum.ewm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.common.enums.Status;
import ru.practicum.ewm.common.model.Request;

import java.util.List;
import java.util.Optional;


public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long requesterId);

    Optional<Request> findByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<Request> findAllByEventId(Long eventId);
    List<Request> findAllByEventIdAndStatus(Long eventId, Status status);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);
}
