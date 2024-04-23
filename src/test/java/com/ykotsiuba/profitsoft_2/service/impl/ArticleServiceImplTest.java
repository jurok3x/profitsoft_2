package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.ArticleDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.entity.Author;
import com.ykotsiuba.profitsoft_2.entity.enums.Field;
import com.ykotsiuba.profitsoft_2.mapper.ArticleMapper;
import com.ykotsiuba.profitsoft_2.mapper.ArticleMapperImpl;
import com.ykotsiuba.profitsoft_2.mapper.AuthorMapper;
import com.ykotsiuba.profitsoft_2.mapper.AuthorMapperImpl;
import com.ykotsiuba.profitsoft_2.repository.ArticleRepository;
import com.ykotsiuba.profitsoft_2.service.ArticleService;
import com.ykotsiuba.profitsoft_2.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ArticleServiceImplTest {

    private ArticleService articleService;

    private ArticleRepository articleRepository;

    private AuthorService authorService;

    private AuthorMapper authorMapper;

    private ArticleMapper articleMapper;

    @BeforeEach
    void setUp() {
        articleRepository = mock(ArticleRepository.class);
        authorService = mock(AuthorService.class);
        authorMapper = new AuthorMapperImpl();
        articleMapper = new ArticleMapperImpl();
        articleService = new ArticleServiceImpl(articleRepository, authorService, articleMapper, authorMapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void save() {
        Article article = prepareArticle();
        Author author = prepareAuthor();
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        when(authorService.findById(any(String.class))).thenReturn(authorMapper.toDTO(author));
    }

    @Test
    void findById() {
        Article article = prepareArticle();
        when(articleRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(article));

        ArticleDTO articleDTO = articleService.findById(UUID.randomUUID().toString());

        assertNotNull(articleDTO);
        assertEquals(article.getField(), articleDTO.getField());
        verify(articleRepository).findById(any(UUID.class));
    }

    private Article prepareArticle() {
        return Article.builder()
                .id(UUID.randomUUID())
                .title("Lasers in our world")
                .author(prepareAuthor())
                .field(Field.PHYSICS)
                .journal("Applied optics")
                .year(2001)
                .build();
    }

    private Author prepareAuthor() {
        return Author.builder()
                .id(UUID.randomUUID())
                .email("johndoe@mail.com")
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    void findById_notFound() {
        when(articleRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> articleService.findById(UUID.randomUUID().toString()));

        verify(articleRepository).findById(any(UUID.class));
    }
    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findBySearchParameters() {
    }

    @Test
    void generateReport() {
    }

    @Test
    void upload() {
    }
}