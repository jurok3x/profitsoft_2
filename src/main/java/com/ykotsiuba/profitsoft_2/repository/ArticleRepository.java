package com.ykotsiuba.profitsoft_2.repository;

import com.ykotsiuba.profitsoft_2.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {


}
