package com.ykotsiuba.profitsoft_2.utils;

import com.ykotsiuba.profitsoft_2.dto.article.*;
import com.ykotsiuba.profitsoft_2.dto.author.SaveAuthorRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.entity.Author;
import com.ykotsiuba.profitsoft_2.entity.enums.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.shaded.com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EntitySource {

    private EntitySource() {}

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

    public static MockMultipartFile prepareJsonFile() throws IOException {
        File json = new File("src/test/resources/static/articles.json");
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.json",
                MediaType.APPLICATION_JSON_VALUE,
                Files.toByteArray(json)
        );
        return file;
    }

    public static ParsingResultDTO prepareParsingResult() {
        ParsingResultDTO resultDTO = new ParsingResultDTO();
        resultDTO.addValidRequest(prepareUploadArticleRequest());
        resultDTO.addInvalidRequest(new UploadArticleRequestDTO());
        return resultDTO;
    }

    public static UploadArticleRequestDTO prepareUploadArticleRequest() {
        return UploadArticleRequestDTO.builder()
                .title("Quantum")
                .authorId("00000000-0000-0000-0000-000000000001")
                .field("PHYSICS")
                .journal("Physics Today")
                .year(2020)
                .build();
    }
}
