package com.ykotsiuba.profitsoft_2.mapper;

import com.ykotsiuba.profitsoft_2.dto.ArticleDTO;
import com.ykotsiuba.profitsoft_2.dto.ArticleResponseDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "author", source = "authorDTO")
    Article toEntity(ArticleDTO articleDTO);

    @Mapping(target = "authorDTO", source = "author")
    ArticleDTO toDTO(Article article);

    @Mapping(target = "authorFullName", expression = "java(mapFullName(article))")
    ArticleResponseDTO toResponseDTO(Article article);

    default String mapFullName(Article article) {
        Author author = article.getAuthor();
        return String.format("%s %s", author.getFirstName(), author.getLastName());
    }
}
