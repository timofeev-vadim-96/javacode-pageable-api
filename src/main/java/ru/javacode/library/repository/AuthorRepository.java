package ru.javacode.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javacode.library.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
