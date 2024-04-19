package com.ykotsiuba.profitsoft_2.dto;

import com.ykotsiuba.profitsoft_2.entity.Field;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSearchDTO {
    private String titlePart;
    private String field;
    private String authorFirstName;
    private String authorLastName;
    private String journal;
    private Integer year;
}
