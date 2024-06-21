package com.ykotsiuba.profitsoft_2.service;

import com.ykotsiuba.profitsoft_2.dto.article.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService {

    /**
     * Saves a new article.
     *
     * @param requestDTO The request object containing article details.
     * @return The saved article DTO.
     */
    ArticleDTO save(SaveArticleRequestDTO requestDTO);

    /**
     * Retrieves an article by its ID.
     *
     * @param id The ID of the article to retrieve.
     * @return The article DTO.
     * @throws jakarta.persistence.EntityNotFoundException if article not found.
     */
    ArticleDTO findById(String id);

    /**
     * Updates an existing article.
     *
     * @param requestDTO The request object containing updated article details.
     * @param id The ID of the article to update.
     * @return The updated article DTO.
     * @throws jakarta.persistence.EntityNotFoundException if article not found.
     */
    ArticleDTO update(SaveArticleRequestDTO requestDTO, String id);

    /**
     * Deletes an article by its ID.
     *
     * @param id The ID of the article to delete.
     * @return The response DTO indicating the deletion status.
     * @throws jakarta.persistence.EntityNotFoundException if article not found.
     */
    ArticleDTO delete(String id);

    /**
     * Searches for articles based on search parameters.
     *
     * @param requestDTO The request object containing search parameters.
     * @return The response DTO containing search results.
     */
    SearchArticlesResponseDTO findBySearchParameters(SearchArticleRequestDTO requestDTO);

    /**
     * Generates file report for articles based on search parameters. Report is sent to response output stream as array of bytes.
     *
     * @param requestDTO The request object containing search parameters.
     */
    void generateReport(ReportArticlesRequestDTO requestDTO, HttpServletResponse response);

    /**
     * Reads JSON file and saves valid articles to the database.
     *
     * @param file JSON file with articles data.
     * @return UploadArticlesResponseDTO response containing count of saved articles, and articles with invalid fields or when user not exists.
     * @throws IllegalArgumentException if received not JSON file.
     */
    UploadArticlesResponseDTO upload(MultipartFile file);
}
