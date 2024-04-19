package com.ykotsiuba.profitsoft_2.service;

import com.ykotsiuba.profitsoft_2.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService {

    ArticleDTO save(SaveArticleRequestDTO requestDTO);

    ArticleDTO findById(String id);

    ArticleDTO update(SaveArticleRequestDTO requestDTO, String id);

    DeleteArticleResponseDTO delete(String id);

    SearchArticlesResponseDTO findBySearchParameters(SearchArticleRequestDTO requestDTO);

    void generateReport(HttpServletResponse response);

    UploadArticlesResponseDTO upload(MultipartFile file);
}
