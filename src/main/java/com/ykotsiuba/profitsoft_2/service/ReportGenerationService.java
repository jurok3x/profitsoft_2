package com.ykotsiuba.profitsoft_2.service;

import com.ykotsiuba.profitsoft_2.dto.article.ArticleResponseDTO;

import java.util.List;

public interface ReportGenerationService {

    /**
     * Generates array of bytes from file containing report information about articles.
     *
     * @param articles The list of articles data.
     */
    byte[] writeReport(List<ArticleResponseDTO> articles);

}
