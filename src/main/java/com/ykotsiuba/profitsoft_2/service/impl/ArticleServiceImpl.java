package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.*;
import com.ykotsiuba.profitsoft_2.service.ArticleService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public class ArticleServiceImpl implements ArticleService {
    @Override
    public ArticleDTO save(SaveArticleRequestDTO requestDTO) {
        return null;
    }

    @Override
    public ArticleDTO findById(String id) {
        return null;
    }

    @Override
    public ArticleDTO update(SaveArticleRequestDTO requestDTO, String id) {
        return null;
    }

    @Override
    public DeleteArticleResponseDTO delete(String id) {
        return null;
    }

    @Override
    public SearchArticlesResponseDTO findBySearchParameters(SearchArticleRequestDTO requestDTO) {
        return null;
    }

    @Override
    public void generateReport(HttpServletResponse response) {

    }

    @Override
    public UploadArticlesResponseDTO upload(MultipartFile file) {
        return null;
    }
}
