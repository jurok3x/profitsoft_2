package com.ykotsiuba.profitsoft_2.service;

import com.ykotsiuba.profitsoft_2.dto.article.ParsingResultDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleParserService {
    ParsingResultDTO parse(MultipartFile file);
}
