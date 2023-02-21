package ru.practicum.ewm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.common.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
