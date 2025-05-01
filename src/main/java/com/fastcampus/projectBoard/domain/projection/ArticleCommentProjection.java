package com.fastcampus.projectBoard.domain.projection;

import com.fastcampus.projectBoard.domain.Article;
import com.fastcampus.projectBoard.domain.ArticleComment;
import com.fastcampus.projectBoard.domain.UserAccount;
import java.time.LocalDateTime;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "withUserAccount", types = ArticleComment.class)
public interface ArticleCommentProjection {
    Long getId();
    UserAccount getUserAccount();
    Long getParentCommentId();
    String getContent();
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getModifiedAt();
    String getModifiedBy();
}
