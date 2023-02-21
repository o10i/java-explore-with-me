package ru.practicum.ewm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.common.model.Request;

import java.util.List;
import java.util.Optional;


public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long requesterId);

    Optional<Request> findByIdAndRequesterId(Long eventId, Long requesterId);

    List<Request> findAllByEventId(Long requesterId);
}
