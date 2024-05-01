package com.ykotsiuba.profitsoft_2.mapper;

import com.ykotsiuba.profitsoft_2.dto.article.ArticleDTO;
import com.ykotsiuba.profitsoft_2.dto.article.ArticleResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.article.ReportArticlesRequestDTO;
import com.ykotsiuba.profitsoft_2.dto.article.SearchArticleRequestDTO;
import com.ykotsiuba.profitsoft_2.dto.author.AuthorDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.entity.enums.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.ykotsiuba.profitsoft_2.utils.EntitySource.*;
import static org.junit.jupiter.api.Assertions.*;

class ArticleMapperTest {

    private ArticleMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ArticleMapperImpl();
    }

    @Test
    void toEntity() {
        ArticleDTO articleDTO = prepareArticleDTO();
        Article article = mapper.toEntity(articleDTO);
        assertTrue(article.getClass().isAssignableFrom(Article.class));
        assertNotNull(article.getId());
        assertNotNull(article.getTitle());
        assertNotNull(article.getJournal());
        assertNotNull(article.getYear());
        assertNotNull(article.getField());
    }

    @Test
    void convertReportRequestToEntity() {
        ReportArticlesRequestDTO requestDTO = prepareReportRequest();
        requestDTO.setAuthorId(null);
        Article article = mapper.convertReportRequestToEntity(requestDTO);
        assertTrue(article.getClass().isAssignableFrom(Article.class));
        assertNull(article.getId());
        assertNull(article.getAuthor());
        assertNotNull(article.getTitle());
        assertNotNull(article.getJournal());
        assertNotNull(article.getYear());
        assertNotNull(article.getField());
    }

    @Test
    void convertSearchRequestToEntity() {
        SearchArticleRequestDTO requestDTO = prepareSearchRequest();
        requestDTO.setAuthorId(null);
        Article article = mapper.convertSearchRequestToEntity(requestDTO);
        assertTrue(article.getClass().isAssignableFrom(Article.class));
        assertNull(article.getId());
        assertNull(article.getAuthor());
        assertNotNull(article.getTitle());
        assertNotNull(article.getJournal());
        assertNotNull(article.getYear());
        assertNotNull(article.getField());
    }

    @Test
    void toDTO() {
        Article article = prepareArticle();
        ArticleDTO articleDTO = mapper.toDTO(article);
        assertTrue(articleDTO.getClass().isAssignableFrom(ArticleDTO.class));
        assertNotNull(articleDTO.getId());
        assertNotNull(articleDTO.getTitle());
        assertNotNull(articleDTO.getJournal());
        assertNotNull(articleDTO.getYear());
        assertNotNull(articleDTO.getField());
    }

    @Test
    void toResponseDTO() {
        Article article = prepareArticle();
        ArticleResponseDTO articleDTO = mapper.toResponseDTO(article);
        assertTrue(articleDTO.getClass().isAssignableFrom(ArticleResponseDTO.class));
        assertNotNull(articleDTO.getTitle());
        assertEquals(articleDTO.getAuthorFullName(), "Keanu Reeves");
        assertNotNull(articleDTO.getYear());
    }
}