package com.ykotsiuba.profitsoft_2.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthorMessages {

    AUTHOR_NOT_FOUND("Author not found for ID: %s"),
    AUTHOR_ALREADY_EXISTS("Author with email %s already exists"),
    AUTHOR_UPDATE_ERROR("Can not update this author"),
    AUTHOR_DELETED("Author deleted successfully.");

    private final String message;
}
