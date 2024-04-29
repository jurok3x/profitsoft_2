package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.author.AuthorDTO;
import com.ykotsiuba.profitsoft_2.dto.author.DeleteAuthorResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.author.SaveAuthorRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Author;
import com.ykotsiuba.profitsoft_2.mapper.AuthorMapper;
import com.ykotsiuba.profitsoft_2.mapper.AuthorMapperImpl;
import com.ykotsiuba.profitsoft_2.repository.AuthorRepository;
import com.ykotsiuba.profitsoft_2.service.AuthorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ykotsiuba.profitsoft_2.entity.enums.AuthorMessages.AUTHOR_DELETED;
import static com.ykotsiuba.profitsoft_2.utils.EntitySource.prepareAuthor;
import static com.ykotsiuba.profitsoft_2.utils.EntitySource.prepareSaveAuthorRequest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthorServiceImplTest {

    private AuthorRepository authorRepository;

    private AuthorService authorService;

    private AuthorMapper authorMapper;


    @BeforeEach
    void setUp() {
        authorRepository = mock(AuthorRepository.class);
        authorMapper = new AuthorMapperImpl();
        authorService = new AuthorServiceImpl(authorRepository, authorMapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void whenFindById_thenReturnCorrectAuthor() {
        Author author = prepareAuthor();
        when(authorRepository.findById(any(UUID.class))).thenReturn(Optional.of(author));

        Optional<Author> response = authorService.findById(UUID.randomUUID().toString());

        assertFalse(response.isEmpty());
        assertEquals(author.getFirstName(), response.get().getFirstName());
        verify(authorRepository).findById(any(UUID.class));
    }

    @Test
    void whenFindByEmail_thenReturnCorrectAuthor() {
        Author author = prepareAuthor();
        when(authorRepository.findByEmail(any(String.class))).thenReturn(Optional.of(author));

        Optional<Author> response = authorService.findByEmail("email@example.com");

        assertFalse(response.isEmpty());
        verify(authorRepository).findByEmail(any(String.class));
    }

    @Test
    void whenFindAll_thenReturnCorrectAuthors() {
        List<Author> authors = Arrays.asList(prepareAuthor());
        when(authorRepository.findAll()).thenReturn(authors);

        List<AuthorDTO> responseList = authorService.findAll();

        assertFalse(responseList.isEmpty());
        verify(authorRepository).findAll();
    }

    @Test
    void whenSave_thenReturnCorrectAuthor() {
        Author author = prepareAuthor();
        SaveAuthorRequestDTO requestDTO = prepareSaveAuthorRequest();
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(authorRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        AuthorDTO responseDTO = authorService.save(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(requestDTO.getEmail(), responseDTO.getEmail());
        verify(authorRepository).findByEmail(any(String.class));
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void whenSaveAuthorWithExistingEmail_thenThrowError() {
        Author author = prepareAuthor();
        SaveAuthorRequestDTO requestDTO = prepareSaveAuthorRequest();
        when(authorRepository.findByEmail(any(String.class))).thenReturn(Optional.of(author));

        assertThrows(IllegalArgumentException.class, () -> authorService.save(requestDTO));

        verify(authorRepository).findByEmail(any(String.class));
    }

    @Test
    void whenUpdate_thenReturnCorrectAuthor() {
        Author author = prepareAuthor();
        SaveAuthorRequestDTO requestDTO = prepareSaveAuthorRequest();
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(authorRepository.findById(any(UUID.class))).thenReturn(Optional.of(author));
        when(authorRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        AuthorDTO responseDTO = authorService.update(requestDTO, author.getId().toString());

        assertNotNull(responseDTO);
        assertEquals(requestDTO.getEmail(), responseDTO.getEmail());
        verify(authorRepository).findByEmail(any(String.class));
        verify(authorRepository).findById(any(UUID.class));
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void whenUpdateAuthorWithExistingEmail_thenThrowError() {
        String existingEmail = "existingmail@example.com";
        Author author = prepareAuthor();
        Author anotherAuthor = prepareAuthor();
        anotherAuthor.setEmail(existingEmail);
        SaveAuthorRequestDTO requestDTO = prepareSaveAuthorRequest();
        requestDTO.setEmail(existingEmail);
        when(authorRepository.findById(any(UUID.class))).thenReturn(Optional.of(author));
        when(authorRepository.findByEmail(any(String.class))).thenReturn(Optional.of(anotherAuthor));

        assertThrows(IllegalArgumentException.class, () -> authorService.update(requestDTO, author.getId().toString()));

        verify(authorRepository).findByEmail(any(String.class));
        verify(authorRepository).findById(any(UUID.class));
    }

    @Test
    void whenDelete_thenReturnCorrectResponse() {
        Author author = prepareAuthor();
        doNothing().when(authorRepository).delete(any(Author.class));
        when(authorRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(author));

        DeleteAuthorResponseDTO responseDTO = authorService.delete(author.getId().toString());

        assertNotNull(responseDTO);
        assertEquals(AUTHOR_DELETED.getMessage(), responseDTO.getMessage());
        verify(authorRepository).delete(any(Author.class));
        verify(authorRepository).findById(any(UUID.class));
    }
}