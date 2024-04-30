package com.ykotsiuba.profitsoft_2.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ykotsiuba.profitsoft_2.dto.article.*;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.entity.Author;
import com.ykotsiuba.profitsoft_2.entity.enums.Field;
import com.ykotsiuba.profitsoft_2.mapper.ArticleMapper;
import com.ykotsiuba.profitsoft_2.repository.ArticleRepository;
import com.ykotsiuba.profitsoft_2.repository.AuthorRepository;
import com.ykotsiuba.profitsoft_2.service.ArticleService;
import com.ykotsiuba.profitsoft_2.service.ReportGenerationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.ykotsiuba.profitsoft_2.entity.enums.ArticleMessages.*;
import static com.ykotsiuba.profitsoft_2.entity.enums.AuthorMessages.AUTHOR_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private Validator validator;

    private static final String FILENAME_HEADER = "attachment; filename=articles.xlsx";

    private final ArticleRepository articleRepository;

    private final AuthorRepository authorRepository;

    private final ArticleMapper articleMapper;

    private final ReportGenerationService reportService;

    @Override
    public ArticleDTO save(SaveArticleRequestDTO requestDTO) {
        Article articleRequest = convertFromSaveRequestDTO(requestDTO);
        Article savedArticle = articleRepository.save(articleRequest);
        return articleMapper.toDTO(savedArticle);
    }

    private Article convertFromSaveRequestDTO(SaveArticleRequestDTO requestDTO) {
        return Article.builder()
                .title(requestDTO.getTitle())
                .field(Field.valueOf(requestDTO.getField()))
                .journal(requestDTO.getJournal())
                .year(requestDTO.getYear())
                .author(findAuthor(requestDTO.getAuthorId()))
                .build();
    }

    private Author findAuthor(String id) {
        Optional<Author> optionalAuthor = authorRepository.findById(UUID.fromString(id));
        return optionalAuthor.orElseThrow(() -> new EntityNotFoundException(String.format(AUTHOR_NOT_FOUND.getMessage(), id)));
    }

    @Override
    public ArticleDTO findById(String id) {
        Article article = findOrThrow(id);
        return articleMapper.toDTO(article);
    }

    private Article findOrThrow(String id) {
        UUID uuid = UUID.fromString(id);
        Optional<Article> optionalAuthor = articleRepository.findById(uuid);
        return optionalAuthor.orElseThrow(() -> new EntityNotFoundException(String.format(ARTICLE_NOT_FOUND.getMessage(), id)));
    }

    @Override
    public ArticleDTO update(SaveArticleRequestDTO requestDTO, String id) {
        Article article = findOrThrow(id);// assume I do not want update non existing articles
        Article articleRequest = convertFromSaveRequestDTO(requestDTO);
        articleRequest.setId(article.getId());
        Article updatedArticle = articleRepository.save(articleRequest);
        return articleMapper.toDTO(updatedArticle);
    }

    @Override
    public DeleteArticleResponseDTO delete(String id) {
        Article article = findOrThrow(id);
        articleRepository.delete(article);
        return DeleteArticleResponseDTO.builder()
                .message(ARTICLE_DELETED.getMessage())
                .build();
    }

    @Override
    public SearchArticlesResponseDTO findBySearchParameters(SearchArticleRequestDTO requestDTO) {
        Pageable pageable = PageRequest.of(requestDTO.getPage(), requestDTO.getSize());
        Page<Article> articlePage = articleRepository.search(requestDTO, pageable);
        int totalPages = articlePage.getTotalPages();
        List<ArticleResponseDTO> list = articlePage.get()
                .map(articleMapper::toResponseDTO)
                .toList();
        return SearchArticlesResponseDTO.builder()
                .list(list)
                .totalPages(totalPages)
                .build();
    }

    @Override
    public void generateReport(ReportArticlesRequestDTO requestDTO, HttpServletResponse response) {
        List<Article> articles = articleRepository.report(requestDTO);
        List<ArticleResponseDTO> report = articles.stream()
                .map(articleMapper::toResponseDTO)
                .toList();
        byte[] bytes = reportService.writeReport(report);
        sendFileResponse(response, bytes);
    }

    private static void sendFileResponse(HttpServletResponse response, byte[] bytes) {
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, FILENAME_HEADER);
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UploadArticlesResponseDTO upload(MultipartFile file) {
        if(file.isEmpty() || file.getContentType() != MediaType.APPLICATION_JSON_VALUE) {
            throw new IllegalArgumentException(FILE_NOT_VALID.getMessage());
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            byte[] fileBytes = file.getBytes();

            List<UploadArticleRequestDTO> requestDTO = objectMapper.readValue(fileBytes, new TypeReference<List<UploadArticleRequestDTO>>() {});

            List<Article> articles = requestDTO
                    .stream()
                    .filter(article -> validator.validate(article).isEmpty())
                    .filter(article -> authorRepository.findById(UUID.fromString(article.getAuthorId())).isPresent())
                    .map(this::convertFromUploadDTO)
                    .toList();
            articleRepository.saveAll(articles);
            return UploadArticlesResponseDTO.builder()
                    .uploaded(articles.size())
                    .errors(requestDTO.size() - articles.size())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Article convertFromUploadDTO(UploadArticleRequestDTO requestDTO) {
        return Article.builder()
                .title(requestDTO.getTitle())
                .field(Field.valueOf(requestDTO.getField()))
                .journal(requestDTO.getJournal())
                .year(requestDTO.getYear())
                .author(findAuthor(requestDTO.getAuthorId()))
                .build();
    }
}
