package com.ykotsiuba.profitsoft_2.dto;

import com.ykotsiuba.profitsoft_2.entity.Field;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ArticleDTO {
    private UUID id;
    private String title;
    private Field field;
    private AuthorDTO authorDTO;
    private Integer year;
    private String journal;
}
