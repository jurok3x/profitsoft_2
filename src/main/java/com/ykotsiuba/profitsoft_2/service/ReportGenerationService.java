package com.ykotsiuba.profitsoft_2.service;

import com.ykotsiuba.profitsoft_2.dto.article.ArticleResponseDTO;

import java.util.List;

public interface ReportGenerationService {

    void writeReport(List<ArticleResponseDTO> articles);

}
