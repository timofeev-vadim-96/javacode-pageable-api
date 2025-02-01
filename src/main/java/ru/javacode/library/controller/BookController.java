package ru.javacode.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javacode.library.controller.dto.BookViewDto;
import ru.javacode.library.service.BookService;
import ru.javacode.library.service.dto.BookDto;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping(value = "/api/v1/book/{id}")
    public ResponseEntity<BookDto> get(@PathVariable("id") long id) {
        BookDto book = bookService.findById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PutMapping("/api/v1/book")
    public ResponseEntity<BookDto> update(@Valid @RequestBody BookViewDto book) {
        BookDto updated = bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenres());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PostMapping("/api/v1/book")
    public ResponseEntity<BookDto> create(@Valid @RequestBody BookViewDto book) {
        BookDto created = bookService.create(book.getTitle(), book.getAuthorId(), book.getGenres());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/book")
    public ResponseEntity<Page<BookDto>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") Direction direction
    ) {
        Page<BookDto> books = bookService.findAll(page, size, sortBy, direction);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/v1/book/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
