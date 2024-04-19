package com.ykotsiuba.profitsoft_2.mapper;

import com.ykotsiuba.profitsoft_2.dto.ArticleDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "author", source = "authorDTO")
    Article toEntity(ArticleDTO articleDTO);

    @Mapping(target = "authorDTO", source = "author")
    ArticleDTO toDTO(Article article);
}
