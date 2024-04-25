package com.ykotsiuba.profitsoft_2.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExcelColumnWidth {
    SMALL(1000),
    MEDIUM(4000),
    BIG(10000);

    private final Integer size;
}
