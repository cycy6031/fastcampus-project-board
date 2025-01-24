package com.fastcampus.projectBoard.dto.response;

import com.fastcampus.projectBoard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectBoard.dto.HashtagDto;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentResponse(
    Long id,
    String title,
    String content,
    Set<String> hashtags,
    LocalDateTime createdAt,
    String email,
    String nickname,
    Set<ArticleCommentResponse> articleCommentResponses
) {
    public static ArticleWithCommentResponse of(Long id, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String email, String nickname, Set<ArticleCommentResponse> articleCommentResponses) {
        return new ArticleWithCommentResponse(id, title, content, hashtags, createdAt, email, nickname, articleCommentResponses);
    }

    public static ArticleWithCommentResponse from(ArticleWithCommentsDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if(nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleWithCommentResponse(
            dto.id(),
            dto.title(),
            dto.content(),
            dto.hashtagDtos().stream()
                .map(HashtagDto::hashtagName)
                .collect(Collectors.toUnmodifiableSet()),
            dto.createdAt(),
            dto.userAccountDto().email(),
            nickname,
            dto.articleCommentDtos().stream()
                .map(ArticleCommentResponse::from)
                .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }
}
