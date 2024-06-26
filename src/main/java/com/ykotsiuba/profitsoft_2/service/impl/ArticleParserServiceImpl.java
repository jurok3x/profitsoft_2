package com.ykotsiuba.profitsoft_2.service.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.ykotsiuba.profitsoft_2.dto.article.ParsingResultDTO;
import com.ykotsiuba.profitsoft_2.dto.article.UploadArticleRequestDTO;
import com.ykotsiuba.profitsoft_2.exception.FileParsingException;
import com.ykotsiuba.profitsoft_2.service.ArticleParserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleParserServiceImpl implements ArticleParserService {

    private final Validator validator;

    @Override
    public ParsingResultDTO parse(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            log.info("Parsing file with name: {}", file.getName());
            JsonFactory jFactory = new JsonFactory();
            JsonParser jParser = jFactory.createParser(reader);
            return readJsonList(jParser);
        } catch (IOException e) {
            log.error("Error parsing file");
            throw new FileParsingException(e.getMessage(), e);
        }
    }

    private ParsingResultDTO readJsonList(JsonParser jParser) throws IOException {
        ParsingResultDTO resultDTO = new ParsingResultDTO();
        while (jParser.nextToken() != JsonToken.END_ARRAY) {
            UploadArticleRequestDTO requestDTO = readArticleRequest(jParser);
            Set<ConstraintViolation<UploadArticleRequestDTO>> violations = validator.validate(requestDTO);
            if (violations.isEmpty()) {
                resultDTO.addValidRequest(requestDTO);
            } else {
                log.error("Invalid upload dto: {}", requestDTO);
                resultDTO.addInvalidRequest(requestDTO);
            }
        }
        jParser.close();
        return resultDTO;
    }

    private UploadArticleRequestDTO readArticleRequest(JsonParser jParser) throws IOException {
        UploadArticleRequestDTO requestDTO = new UploadArticleRequestDTO();
        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jParser.getCurrentName();
            if(validParameter(fieldName)) {//assume we skip unknown parameters
                jParser.nextToken();
                String parsedValue = jParser.getText();
                setField(requestDTO, fieldName, parsedValue);
            }
        }
        return requestDTO;
    }

    private boolean validParameter(String fieldName) {
        if (fieldName == null) {
            return false;
        }
        return Arrays.stream(UploadArticleRequestDTO.class.getDeclaredFields())
                .map(Field::getName)
                .anyMatch(fieldName::equals);
    }

    private void setField(UploadArticleRequestDTO requestDTO, String parameterName, String parsedValue) {
        if (parameterName != null) {
            switch (parameterName) {
                case "title" -> requestDTO.setTitle(parsedValue);
                case "authorId" -> requestDTO.setAuthorId(parsedValue);
                case "year" -> requestDTO.setYear(Integer.valueOf(parsedValue));
                case "field" -> requestDTO.setField(parsedValue);
                case "journal" -> requestDTO.setJournal(parsedValue);
                default -> {}
            }
        }
    }
}
