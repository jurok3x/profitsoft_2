package com.ykotsiuba.profitsoft_2.service;

import com.ykotsiuba.profitsoft_2.dto.article.ParsingResultDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleParserService {

    /**
     * Parses JSON and returns list of validated and not valid articles.
     *
     * @param file JSON file with articles data.
     * @return ParsingResultDTO response containing list valid, and invalid articles.
     */
    ParsingResultDTO parse(MultipartFile file);
}
