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
public class SaveArticleRequestDTO {
    private String title;
    private Field field;
    private SaveAuthorRequestDTO authorDTO;
    private Integer year;
    private String journal;
}
