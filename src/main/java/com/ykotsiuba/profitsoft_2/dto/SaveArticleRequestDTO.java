package com.ykotsiuba.profitsoft_2.dto;

import com.ykotsiuba.profitsoft_2.entity.Field;
import com.ykotsiuba.profitsoft_2.validation.annotation.ValidEnum;
import com.ykotsiuba.profitsoft_2.validation.annotation.ValidYear;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveArticleRequestDTO {
    @NotBlank(message = "title is required")
    private String title;

    @NotNull(message = "field is required")
    @ValidEnum(enumClass = Field.class)
    private String field;

    @NotBlank(message = "author is required")
    private String authorId;

    @NotNull(message = "year is required")
    @ValidYear
    private Integer year;

    @NotBlank(message = "journal is required")
    private String journal;
}
