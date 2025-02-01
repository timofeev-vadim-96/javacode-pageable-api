package ru.javacode.library.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.javacode.library.model.Author;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private Long id;

    private String fullName;

    public AuthorDto(Author author) {
        id = author.getId();
        fullName = author.getFullName();
    }
}