package com.ykotsiuba.profitsoft_2.dto.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ykotsiuba.profitsoft_2.dto.author.AuthorDTO;
import com.ykotsiuba.profitsoft_2.entity.enums.Field;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

    private UUID id;

    private String title;

    private Field field;

    @JsonProperty("author")
    private AuthorDTO authorDTO;

    private Integer year;

    private String journal;
}
