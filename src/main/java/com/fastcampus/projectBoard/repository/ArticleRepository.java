package com.fastcampus.projectBoard.repository;

import com.fastcampus.projectBoard.domain.Article;
import com.fastcampus.projectBoard.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository
    extends JpaRepository<Article, Long>
    , QuerydslPredicateExecutor<Article>
    , QuerydslBinderCustomizer<QArticle>
{

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {

    }

    Object findByTitle(String searchKeyword, Pageable pageable);
}