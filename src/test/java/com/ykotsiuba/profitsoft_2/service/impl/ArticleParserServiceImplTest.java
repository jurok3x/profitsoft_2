package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.article.ParsingResultDTO;
import com.ykotsiuba.profitsoft_2.dto.article.UploadArticleRequestDTO;
import com.ykotsiuba.profitsoft_2.service.ArticleParserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

import static com.ykotsiuba.profitsoft_2.utils.EntitySource.prepareJsonFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ArticleParserServiceImplTest {

    private ArticleParserService parserService;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
        parserService = new ArticleParserServiceImpl(validator);
    }

    @Test
    void testParsing() throws IOException {
        final int expectedValid = 4;
        final int expectedInvalid = 6;
        MultipartFile file = prepareJsonFile();
        ParsingResultDTO resultDTO = parserService.parse(file);
        assertEquals(expectedValid, resultDTO.getValidRequests().size());
        assertEquals(expectedInvalid, resultDTO.getInvalidRequests().size());
    }

    @Test
    void validate() throws IOException {
        UploadArticleRequestDTO requestDTO = new UploadArticleRequestDTO();
        requestDTO.setYear(2222);
        Set<ConstraintViolation<UploadArticleRequestDTO>> violations = validator.validate(requestDTO);
        assertFalse(violations.isEmpty());
    }

}