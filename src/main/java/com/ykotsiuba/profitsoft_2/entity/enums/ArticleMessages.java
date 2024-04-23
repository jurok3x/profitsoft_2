package com.ykotsiuba.profitsoft_2.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ArticleMessages {

    ARTICLE_DELETED("Article deleted successfully."),
    ARTICLE_NOT_FOUND("Article not found for ID: %s");

    private final String message;
}
