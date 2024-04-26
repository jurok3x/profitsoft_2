package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.article.ArticleResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.article.ReportArticlesRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.service.ReportGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.ykotsiuba.profitsoft_2.utils.EntitySource.*;
import static org.junit.jupiter.api.Assertions.*;

class ExcelReportGenerationServiceTest {

    private ReportGenerationService reportService;

    @BeforeEach
    void setUp() {
        reportService = new ExcelReportGenerationService();
    }

    @Test
    void whenGenerateReport_thenResultNotEmpty() {
        List<ArticleResponseDTO> articles = Arrays.asList(prepareArticleResponse());
        byte[] bytes = reportService.writeReport(articles);

        assertNotNull(bytes);
        assertFalse(bytes.length == 0);
    }

}