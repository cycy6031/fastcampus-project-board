package com.fastcampus.projectBoard.repository;

import com.fastcampus.projectBoard.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}