package com.ykotsiuba.profitsoft_2.mapper;

import com.ykotsiuba.profitsoft_2.dto.article.ArticleDTO;
import com.ykotsiuba.profitsoft_2.dto.article.ArticleResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.article.ReportArticlesRequestDTO;
import com.ykotsiuba.profitsoft_2.dto.article.SearchArticleRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "author", source = "authorDTO")
    Article toEntity(ArticleDTO articleDTO);

    @Mapping(target = "author", expression = "java(mapReportAuthorId(articleDTO))")
    @Mapping(target = "id", ignore = true)
    Article convertReportRequestToEntity(ReportArticlesRequestDTO articleDTO);

    @Mapping(target = "author", expression = "java(mapSearchAuthorId(articleDTO))")
    @Mapping(target = "id", ignore = true)
    Article convertSearchRequestToEntity(SearchArticleRequestDTO articleDTO);

    @Mapping(target = "authorDTO", source = "author")
    ArticleDTO toDTO(Article article);

    @Mapping(target = "authorFullName", expression = "java(mapFullName(article))")
    ArticleResponseDTO toResponseDTO(Article article);

    default String mapFullName(Article article) {
        Author author = article.getAuthor();
        return String.format("%s %s", author.getFirstName(), author.getLastName());
    }

    default Author mapReportAuthorId(ReportArticlesRequestDTO articleDTO) {
        if(articleDTO.getAuthorId() == null) {
            return null;
        }
        return Author.builder()
                .id(UUID.fromString(articleDTO.getAuthorId()))
                .build();
    }

    default Author mapSearchAuthorId(SearchArticleRequestDTO articleDTO) {
        if(articleDTO.getAuthorId() == null) {
            return null;
        }
        return Author.builder()
                .id(UUID.fromString(articleDTO.getAuthorId()))
                .build();
    }
}
