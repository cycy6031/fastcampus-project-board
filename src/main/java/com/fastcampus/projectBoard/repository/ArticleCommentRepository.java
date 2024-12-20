package com.fastcampus.projectBoard.repository;

import com.fastcampus.projectBoard.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long>{

}
