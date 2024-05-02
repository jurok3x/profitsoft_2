package com.ykotsiuba.profitsoft_2.mapper;

import com.ykotsiuba.profitsoft_2.dto.author.AuthorDTO;
import com.ykotsiuba.profitsoft_2.dto.author.SaveAuthorRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ykotsiuba.profitsoft_2.utils.EntitySource.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthorMapperTest {

    private AuthorMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AuthorMapperImpl();
    }

    @Test
    void toEntity() {
        AuthorDTO authorDTO = prepareAuthorDTO();
        Author author = mapper.toEntity(authorDTO);
        assertTrue(author.getClass().isAssignableFrom(Author.class));
        assertNotNull(author.getId());
        assertNotNull(author.getFirstName());
        assertNotNull(author.getLastName());
        assertNotNull(author.getEmail());
    }

    @Test
    void toDTO() {
        Author author = prepareAuthor();
        AuthorDTO authorDTO = mapper.toDTO(author);
        assertTrue(authorDTO.getClass().isAssignableFrom(AuthorDTO.class));
        assertNotNull(authorDTO.getId());
        assertNotNull(authorDTO.getFirstName());
        assertNotNull(authorDTO.getLastName());
        assertNotNull(authorDTO.getEmail());
    }

    @Test
    void convertSaveRequestToEntity() {
        SaveAuthorRequestDTO requestDTO = prepareSaveAuthorRequest();
        Author author = mapper.convertSaveRequestToEntity(requestDTO);
        assertTrue(author.getClass().isAssignableFrom(Author.class));
        assertNull(author.getId());
        assertNotNull(author.getFirstName());
        assertNotNull(author.getLastName());
        assertNotNull(author.getEmail());
    }

}