package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.article.*;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.entity.Author;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

import static com.ykotsiuba.profitsoft_2.entity.enums.ArticleMessages.ARTICLE_DELETED;
import static com.ykotsiuba.profitsoft_2.utils.EntitySource.*;
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
    void whenSave_thenReturnCorrectArticle() {
        Article article = prepareArticle();
        Author author = prepareAuthor();
        SaveArticleRequestDTO requestDTO = prepareSaveArticleRequest();
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        when(authorService.findById(any(String.class))).thenReturn(authorMapper.toDTO(author));

        ArticleDTO responseDTO = articleService.save(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(article.getField(), responseDTO.getField());
        verify(articleRepository).save(any(Article.class));
        verify(authorService).findById(any(String.class));
    }

    @Test
    void whenAuthorNotFound_thenThrowException() {
        SaveArticleRequestDTO requestDTO = prepareSaveArticleRequest();
        when(authorService.findById(any(String.class))).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> articleService.save(requestDTO));

        verifyNoInteractions(articleRepository);
        verify(authorService).findById(any(String.class));
    }

    @Test
    void whenFindById_thenReturnCorrectArticle() {
        Article article = prepareArticle();
        when(articleRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(article));

        ArticleDTO responseDTO = articleService.findById(UUID.randomUUID().toString());

        assertNotNull(responseDTO);
        assertEquals(article.getField(), responseDTO.getField());
        verify(articleRepository).findById(any(UUID.class));
    }

    @Test
    void whenArticleNotFound_thenThrowException() {
        when(articleRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> articleService.findById(UUID.randomUUID().toString()));

        verify(articleRepository).findById(any(UUID.class));
    }

    @Test
    void whenUpdate_thenReturnCorrectArticle() {
        Article article = prepareArticle();
        Author author = prepareAuthor();
        SaveArticleRequestDTO requestDTO = prepareSaveArticleRequest();
        when(articleRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(article));
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        when(authorService.findById(any(String.class))).thenReturn(authorMapper.toDTO(author));

        ArticleDTO responseDTO = articleService.update(requestDTO, UUID.randomUUID().toString());

        assertNotNull(responseDTO);
        assertEquals(article.getField(), responseDTO.getField());
        verify(articleRepository).save(any(Article.class));
        verify(articleRepository).findById(any(UUID.class));
        verify(authorService).findById(any(String.class));
    }

    @Test
    void whenUpdatedArticleNotFound_thenThrowException() {
        SaveArticleRequestDTO requestDTO = prepareSaveArticleRequest();
        when(articleRepository.findById(any(UUID.class))).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> articleService.update(requestDTO, UUID.randomUUID().toString()));

        verify(articleRepository).findById(any(UUID.class));
        verifyNoInteractions(authorService);
    }

    @Test
    void whenDelete_thenReturnCorrectResponse() {
        Article article = prepareArticle();
        doNothing().when(articleRepository).delete(any(Article.class));
        when(articleRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(article));

        DeleteArticleResponseDTO responseDTO = articleService.delete(UUID.randomUUID().toString());

        assertNotNull(responseDTO);
        assertEquals(ARTICLE_DELETED.getMessage(), responseDTO.getMessage());
        verify(articleRepository).delete(any(Article.class));
        verify(articleRepository).findById(any(UUID.class));
    }

    @Test
    void whenFindBySearchParameters_thenReturnArticleList() {
        SearchArticleRequestDTO requestDTO = prepareSearchRequest();
        Pageable pageRequest = PageRequest.of(0, 10);
        Page<Article> page = prepareTestPage(pageRequest);
        when(articleRepository.search(any(SearchArticleRequestDTO.class), any(Pageable.class))).thenReturn(page);

        SearchArticlesResponseDTO responseDTO = articleService.findBySearchParameters(requestDTO);

        assertNotNull(responseDTO);
        assertFalse(responseDTO.getList().isEmpty());
        verify(articleRepository).search(any(SearchArticleRequestDTO.class), any(Pageable.class));
    }

    @Test
    void generateReport() {
    }

    @Test
    void upload() {
    }
}