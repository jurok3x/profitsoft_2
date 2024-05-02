package com.ykotsiuba.profitsoft_2.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchArticlesResponseDTO {

    private List<ArticleResponseDTO> list;

    private Integer totalPages;
}
