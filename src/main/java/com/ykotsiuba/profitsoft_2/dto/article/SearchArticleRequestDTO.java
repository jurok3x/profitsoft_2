package com.ykotsiuba.profitsoft_2.dto.article;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchArticleRequestDTO {
    private String title;
    private String field;
    private String authorId;
    private String journal;
    private Integer year;

    @NotNull(message = "page is required")
    @PositiveOrZero(message = "invalid page value")
    private Integer page;

    @NotNull(message = "size is required")
    @Positive(message = "invalid size value")
    private Integer size;
}
