package ru.javacode.library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import ru.javacode.library.service.dto.BookDto;

import java.util.Set;

public interface BookService {
    BookDto findById(long id);

    Page<BookDto> findAll(int page, int size, String sortBy, Direction direction);

    BookDto create(String title, long authorId, Set<Long> genresIds);

    BookDto update(Long id, String title, long authorId, Set<Long> genresIds);

    void deleteById(long id);
}