package com.ykotsiuba.profitsoft_2.repository;

import com.ykotsiuba.profitsoft_2.dto.SearchArticleRequestDTO;
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

    @Query("SELECT a FROM Article a WHERE (:#{#search.field} is null or a.field = :#{#search.field})"
    + "and (:#{#search.journal} is null or a.journal = :#{#search.journal})")
    List<Article> search(@Param("search") SearchArticleRequestDTO searchDTO);

}
