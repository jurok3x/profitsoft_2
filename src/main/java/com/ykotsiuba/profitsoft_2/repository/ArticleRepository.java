package com.ykotsiuba.profitsoft_2.repository;

import com.ykotsiuba.profitsoft_2.dto.article.ReportArticlesRequestDTO;
import com.ykotsiuba.profitsoft_2.dto.article.SearchArticleRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    @Query("SELECT a FROM Article a WHERE "
            + "(:#{#search.field} is null or CAST(a.field as string) = :#{#search.field})"
            + "and (:#{#search.journal} is null or a.journal = :#{#search.journal})"
            + "and (:#{#search.year} is null or a.year = :#{#search.year})"
            + "and (:#{#search.authorId} is null or CAST(a.author.id as string) = :#{#search.authorId})"
            + "and (:#{#search.titlePart} is null or lower(a.title) like lower(concat('%', :#{#search.titlePart}, '%')))"
    )
    Page<Article> search(@Param("search") SearchArticleRequestDTO searchDTO, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE "
            + "(:#{#search.field} is null or CAST(a.field as string) = :#{#search.field})"
            + "and (:#{#search.journal} is null or a.journal = :#{#search.journal})"
            + "and (:#{#search.year} is null or a.year = :#{#search.year})"
            + "and (:#{#search.authorId} is null or CAST(a.author.id as string) = :#{#search.authorId})"
            + "and (:#{#search.titlePart} is null or lower(a.title) like lower(concat('%', :#{#search.titlePart}, '%')))"
    )
    List<Article> report(@Param("search") ReportArticlesRequestDTO searchDTO);

}
