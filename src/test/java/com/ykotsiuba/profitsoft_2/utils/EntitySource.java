package com.ykotsiuba.profitsoft_2.utils;

import com.ykotsiuba.profitsoft_2.dto.article.ArticleResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.article.ReportArticlesRequestDTO;
import com.ykotsiuba.profitsoft_2.dto.article.SaveArticleRequestDTO;
import com.ykotsiuba.profitsoft_2.dto.article.SearchArticleRequestDTO;
import com.ykotsiuba.profitsoft_2.dto.author.SaveAuthorRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.entity.Author;
import com.ykotsiuba.profitsoft_2.entity.enums.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EntitySource {
    public static SaveAuthorRequestDTO prepareSaveAuthorRequest() {
        return SaveAuthorRequestDTO.builder()
                .firstName("Keanu")
                .lastName("Reeves")
                .email("kreeves@example.com")
                .build();
    }

    public static Author prepareAuthor() {
        return Author.builder()
                .id(UUID.randomUUID())
                .email("kreeves@example.com")
                .firstName("Keanu")
                .lastName("Reeves")
                .build();
    }

    public static Article prepareArticle() {
        return Article.builder()
                .id(UUID.randomUUID())
                .title("Lasers in our world")
                .author(prepareAuthor())
                .field(Field.PHYSICS)
                .journal("Applied optics")
                .year(2001)
                .build();
    }

    public static SaveArticleRequestDTO prepareSaveArticleRequest() {
        return SaveArticleRequestDTO.builder()
                .title("Lasers in our world")
                .authorId("00000000-0000-0000-0000-000000000001")
                .field(Field.PHYSICS.name())
                .journal("Applied optics")
                .year(2001)
                .build();
    }

    public static SearchArticleRequestDTO prepareSearchRequest() {
        return SearchArticleRequestDTO.builder()
                .page(0)
                .size(10)
                .titlePart("quantum")
                .authorId("00000000-0000-0000-0000-000000000001")
                .field("PHYSICS")
                .journal("Physics Today")
                .year(2020)
                .build();
    }

    public static ReportArticlesRequestDTO prepareReportRequest() {
        return ReportArticlesRequestDTO.builder()
                .titlePart("quantum")
                .authorId("00000000-0000-0000-0000-000000000001")
                .field("PHYSICS")
                .journal("Physics Today")
                .year(2020)
                .build();
    }

    public static ArticleResponseDTO prepareArticleResponse() {
        return ArticleResponseDTO.builder()
                .title("Lasers in our world")
                .authorFullName("Jonh Doe")
                .year(2020)
                .build();
    }

    public static  Page<Article> prepareTestPage(Pageable pageRequest) {
        List<Article> articles = Arrays.asList(prepareArticle());
        return new PageImpl<>(articles, pageRequest, articles.size());
    }
}
