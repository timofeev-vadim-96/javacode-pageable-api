package ru.javacode.library.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.javacode.library.model.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    private Long id;

    private String name;

    public GenreDto(Genre genre) {
        id = genre.getId();
        name = genre.getName();
    }
}