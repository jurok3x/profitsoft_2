package com.ykotsiuba.profitsoft_2.dto.article;

import com.ykotsiuba.profitsoft_2.entity.enums.Field;
import com.ykotsiuba.profitsoft_2.validation.annotation.ValidEnum;
import com.ykotsiuba.profitsoft_2.validation.annotation.ValidYear;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", message = "invalid UUID format")
    private String authorId;

    @NotNull(message = "year is required")
    @ValidYear
    private Integer year;

    @NotBlank(message = "journal is required")
    private String journal;
}
