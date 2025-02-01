package ru.javacode.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javacode.library.model.Genre;

import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT g from Genre g WHERE g.id IN :ids")
    Set<Genre> findAllByIds(@Param("ids") Set<Long> ids);
}
