package com.ykotsiuba.profitsoft_2.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ArticleMessages {

    ARTICLE_DELETED("Article deleted successfully."),
    ARTICLE_NOT_FOUND("Article not found for ID: %s"),
    FILE_NOT_VALID("Provided file is not valid. Valid format is application/json");

    private final String message;
}
