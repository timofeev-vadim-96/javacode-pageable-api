package ru.javacode.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javacode.library.exception.EntityNotFoundException;
import ru.javacode.library.exception.IdNullPointerException;
import ru.javacode.library.model.Book;
import ru.javacode.library.model.Genre;
import ru.javacode.library.repository.AuthorRepository;
import ru.javacode.library.repository.BookRepository;
import ru.javacode.library.repository.GenreRepository;
import ru.javacode.library.service.dto.BookDto;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "bookById", key = "#id")
    public BookDto findById(long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));

        return new BookDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("books")
    public Page<BookDto> findAll(int page, int size, String sortBy, Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Book> books = bookRepository.findAll(pageable);

        return new PageImpl<>(
                books.getContent().stream().map(BookDto::new).collect(Collectors.toList()),
                pageable,
                books.getTotalElements()
        );
    }

    @Override
    @Transactional
    @Caching(
            evict = {@CacheEvict(value = "books", allEntries = true)},
            put = {@CachePut(value = "bookById", key = "#result.id")})
    public BookDto create(String title, long authorId, Set<Long> genresIds) {
        Book created = save(null, title, authorId, genresIds);

        return new BookDto(created);
    }

    @Override
    @Transactional
    @Caching(
            evict = {@CacheEvict(value = "books", allEntries = true)},
            put = {@CachePut(value = "bookById", key = "#result.id")})
    public BookDto update(Long id, String title, long authorId, Set<Long> genresIds) {
        if (id == null) {
            throw new IdNullPointerException("Book id must not be null");
        }
        Book updated = save(id, title, authorId, genresIds);

        return new BookDto(updated);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "books", allEntries = true),
            @CacheEvict(value = "bookById", key = "#id")})
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(Long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = getGenres(genresIds);

        Book book;
        if (id != null) {
            book = bookRepository
                    .findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Book with id = %d not found".formatted(id)));
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenres(genres);
        } else {
            book = new Book(null, title, author, genres);
        }

        return bookRepository.save(book);
    }

    private Set<Genre> getGenres(Set<Long> genresIds) {
        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }
        return genres;
    }
}