package ru.javacode.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javacode.library.model.User;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
