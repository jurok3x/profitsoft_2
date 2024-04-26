package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.article.ArticleResponseDTO;
import com.ykotsiuba.profitsoft_2.service.ReportGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.ykotsiuba.profitsoft_2.utils.EntitySource.prepareArticleResponse;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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