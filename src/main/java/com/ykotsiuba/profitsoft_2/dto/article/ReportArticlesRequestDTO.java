package com.ykotsiuba.profitsoft_2.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportArticlesRequestDTO {
    private String title;
    private String field;
    private String authorId;
    private String journal;
    private Integer year;
}
