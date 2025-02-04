package ru.javacode.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javacode.library.controller.dto.BookViewDto;
import ru.javacode.library.exception.EntityNotFoundException;
import ru.javacode.library.exception.IdNullPointerException;
import ru.javacode.library.security.filter.JwtAuthenticationFilter;
import ru.javacode.library.service.BookService;
import ru.javacode.library.service.dto.AuthorDto;
import ru.javacode.library.service.dto.BookDto;
import ru.javacode.library.service.dto.GenreDto;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BookController.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@DisplayName("Контроллер для работы с книгами")
class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    private BookDto book;

    private BookViewDto dto;

    @BeforeEach
    void setUp() {
        long bookId = 1L;
        AuthorDto author = new AuthorDto(1L, "Author_1");
        book = new BookDto(bookId, "Book_1", author, List.of(new GenreDto(1L, "Genre_1")));
        dto = new BookViewDto(bookId, book.getTitle(), author.getId(), Set.of(1L));
    }

    @Test
    void get() throws Exception {
        long id = book.getId();
        when(bookService.findById(id)).thenReturn(book);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/book/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
        verify(bookService, times(1)).findById(id);
    }

    @Test
    void update() throws Exception {
        when(bookService.update(book.getId(), dto.getTitle(), dto.getAuthorId(), dto.getGenres()))
                .thenReturn(book);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));

        verify(bookService, times(1))
                .update(book.getId(), dto.getTitle(), dto.getAuthorId(), dto.getGenres());
    }

    @Test
    void updateInvalidWhenBookDoesNotExists() throws Exception {
        when(bookService.update(dto.getId(), dto.getTitle(), dto.getAuthorId(), dto.getGenres()))
                .thenThrow(new EntityNotFoundException("exc"));

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());

        verify(bookService, times(1))
                .update(dto.getId(), dto.getTitle(), dto.getAuthorId(), dto.getGenres());
    }

    @Test
    void updateInvalidWhenIdIsNull() throws Exception {
        when(bookService.update(null, dto.getTitle(), dto.getAuthorId(), dto.getGenres()))
                .thenThrow(IdNullPointerException.class);
        dto.setId(null);
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(bookService, times(1))
                .update(dto.getId(), dto.getTitle(), dto.getAuthorId(), dto.getGenres());
    }

    @Test
    void create() throws Exception {
        when(bookService.create(dto.getTitle(), dto.getAuthorId(), dto.getGenres()))
                .thenReturn(book);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(book)));

        verify(bookService, times(1))
                .create(dto.getTitle(), dto.getAuthorId(), dto.getGenres());


    }

    @Test
    void createInvalid() throws Exception {
        when(bookService.create(dto.getTitle(), dto.getAuthorId(), dto.getGenres()))
                .thenThrow(new EntityNotFoundException("exc"));

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());

        verify(bookService, times(1))
                .create(dto.getTitle(), dto.getAuthorId(), dto.getGenres());
    }

    @Test
    void getAll() throws Exception {
        List<BookDto> expected = List.of(book);
        final int page = 0;
        final int size = 10;
        final String sortBy = "id";
        final Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        PageImpl<BookDto> books = new PageImpl<>(expected, pageable, expected.size());
        when(bookService.findAll(page, size, sortBy, direction))
                .thenReturn(books);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/book")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("direction", direction.name())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(books)));
        verify(bookService, times(1)).findAll(page, size, sortBy, direction);
    }

    @Test
    void delete() throws Exception {
        long id = 1L;
        doNothing().when(bookService).deleteById(id);

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/book/{id}", id))
                .andExpect(status().isOk());
        verify(bookService, times(1)).deleteById(id);
    }
}