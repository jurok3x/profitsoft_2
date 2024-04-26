package com.ykotsiuba.profitsoft_2.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExcelColumnWidth {
    TINY(1000),
    SMALL(2000),
    MEDIUM(4000),
    BIG(10000);

    private final Integer size;
}
