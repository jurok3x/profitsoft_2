package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.article.*;
import com.ykotsiuba.profitsoft_2.dto.author.AuthorDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.entity.Author;
import com.ykotsiuba.profitsoft_2.entity.enums.Field;
import com.ykotsiuba.profitsoft_2.mapper.ArticleMapper;
import com.ykotsiuba.profitsoft_2.mapper.AuthorMapper;
import com.ykotsiuba.profitsoft_2.repository.ArticleRepository;
import com.ykotsiuba.profitsoft_2.service.ArticleService;
import com.ykotsiuba.profitsoft_2.service.AuthorService;
import com.ykotsiuba.profitsoft_2.service.ReportGenerationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ykotsiuba.profitsoft_2.entity.enums.ArticleMessages.ARTICLE_DELETED;
import static com.ykotsiuba.profitsoft_2.entity.enums.ArticleMessages.ARTICLE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private static final String FILENAME_HEADER = "attachment; filename=articles.xlsx";
    @Value("${file.location:articles.xml}")
    private String excelFileLocation;

    private final ArticleRepository articleRepository;

    private final AuthorService authorService;

    private final ArticleMapper articleMapper;

    private final AuthorMapper authorMapper;

    private final ReportGenerationService reportService;

    @Override
    public ArticleDTO save(SaveArticleRequestDTO requestDTO) {
        Article articleRequest = prepareArticle(requestDTO);
        Article savedArticle = articleRepository.save(articleRequest);
        return articleMapper.toDTO(savedArticle);
    }

    private Article prepareArticle(SaveArticleRequestDTO requestDTO) {
        return Article.builder()
                .title(requestDTO.getTitle())
                .field(Field.valueOf(requestDTO.getField()))
                .journal(requestDTO.getJournal())
                .year(requestDTO.getYear())
                .author(findAuthor(requestDTO.getAuthorId()))
                .build();
    }

    private Author findAuthor(String id) {
        AuthorDTO authorDTO = authorService.findById(id);
        return authorMapper.toEntity(authorDTO);
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
        Article articleRequest = prepareArticle(requestDTO);
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
        return null;
    }
}
