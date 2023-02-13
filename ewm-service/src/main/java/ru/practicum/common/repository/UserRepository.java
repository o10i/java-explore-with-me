package ru.practicum.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.common.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
