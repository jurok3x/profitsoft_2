package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.article.*;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.entity.Author;
import com.ykotsiuba.profitsoft_2.mapper.ArticleMapper;
import com.ykotsiuba.profitsoft_2.mapper.ArticleMapperImpl;
import com.ykotsiuba.profitsoft_2.repository.ArticleRepository;
import com.ykotsiuba.profitsoft_2.repository.AuthorRepository;
import com.ykotsiuba.profitsoft_2.service.ArticleParserService;
import com.ykotsiuba.profitsoft_2.service.ArticleService;
import com.ykotsiuba.profitsoft_2.service.ReportGenerationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ykotsiuba.profitsoft_2.entity.enums.ArticleMessages.ARTICLE_DELETED;
import static com.ykotsiuba.profitsoft_2.utils.EntitySource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class ArticleServiceImplTest {

    private ArticleService articleService;

    private ArticleRepository articleRepository;

    private AuthorRepository authorRepository;

    private ArticleMapper articleMapper;

    private ReportGenerationService reportService;

    private ArticleParserService parserService;

    @BeforeEach
    void setUp() {
        articleRepository = mock(ArticleRepository.class);
        reportService = mock(ReportGenerationService.class);
        authorRepository = mock(AuthorRepository.class);
        articleMapper = new ArticleMapperImpl();
        parserService = mock(ArticleParserServiceImpl.class);
        articleService = new ArticleServiceImpl(articleRepository, authorRepository, articleMapper, reportService, parserService);
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
        when(authorRepository.findById(any(UUID.class))).thenReturn(Optional.of(author));

        ArticleDTO responseDTO = articleService.save(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(article.getField(), responseDTO.getField());
        verify(articleRepository).save(any(Article.class));
        verify(authorRepository).findById(any(UUID.class));
    }

    @Test
    void whenAuthorNotFound_thenThrowException() {
        SaveArticleRequestDTO requestDTO = prepareSaveArticleRequest();
        when(authorRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> articleService.save(requestDTO));

        verifyNoInteractions(articleRepository);
        verify(authorRepository).findById(any(UUID.class));
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
        when(authorRepository.findById(any(UUID.class))).thenReturn(Optional.of(author));

        ArticleDTO responseDTO = articleService.update(requestDTO, UUID.randomUUID().toString());

        assertNotNull(responseDTO);
        assertEquals(article.getField(), responseDTO.getField());
        verify(articleRepository).save(any(Article.class));
        verify(articleRepository).findById(any(UUID.class));
        verify(authorRepository).findById(any(UUID.class));
    }

    @Test
    void whenUpdatedArticleNotFound_thenThrowException() {
        SaveArticleRequestDTO requestDTO = prepareSaveArticleRequest();
        when(articleRepository.findById(any(UUID.class))).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> articleService.update(requestDTO, UUID.randomUUID().toString()));

        verify(articleRepository).findById(any(UUID.class));
        verifyNoInteractions(authorRepository);
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
    void whenGenerateReport_thenVerifyMethods() {
        ReportArticlesRequestDTO requestDTO = prepareReportRequest();
        List<Article> articles = Arrays.asList(prepareArticle());
        byte[] bytes = {};
        when(reportService.writeReport(any())).thenReturn(bytes);
        when(articleRepository.report(any(ReportArticlesRequestDTO.class))).thenReturn(articles);

        MockHttpServletResponse response = new MockHttpServletResponse();
        articleService.generateReport(requestDTO, response);

        verify(articleRepository).report(any(ReportArticlesRequestDTO.class));
        verify(reportService).writeReport(any());
    }

    @Test
    void whenUpload_thenReturnCorrectResponse() throws IOException {
        MockMultipartFile file = prepareJsonFile();
        ParsingResultDTO resultDTO = prepareParsingResult();
        List<Article> articles = Arrays.asList(prepareArticle());
        when(parserService.parse(any(MockMultipartFile.class))).thenReturn(resultDTO);
        when(articleRepository.saveAll(any())).thenReturn(articles);

        UploadArticlesResponseDTO responseDTO = articleService.upload(file);

        assertEquals(1, responseDTO.getUploaded());
        assertEquals(1, responseDTO.getErrors());
        verify(articleRepository).saveAll(any());
        verify(parserService).parse(any(MockMultipartFile.class));
    }
}