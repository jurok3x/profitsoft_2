package com.ykotsiuba.profitsoft_2.repository;

import com.ykotsiuba.profitsoft_2.dto.ArticleSearchDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    @Query("SELECT a FROM articles c WHERE (:q.field is null or a.field = :q.field) and (:q.journal is null"
            + " or a.journal = :q.journal)")
    Page<Article> findArticlesByFilter(@Param("q") ArticleSearchDTO searchDTO, Pageable pageable);

}
