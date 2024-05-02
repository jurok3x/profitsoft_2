package com.ykotsiuba.profitsoft_2.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExcelHeader {

    NUMBER("№"),
    TITLE("Title"),
    AUTHOR("Author"),
    YEAR("Year");

    private final String name;
}
