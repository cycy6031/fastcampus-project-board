package com.fastcampus.projectBoard.dto.response;

import com.fastcampus.projectBoard.dto.ArticleCommentDto;
import com.fastcampus.projectBoard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectBoard.dto.HashtagDto;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public record ArticleWithCommentResponse(
    Long id,
    String title,
    String content,
    Set<String> hashtags,
    LocalDateTime createdAt,
    String email,
    String nickname,
    Set<ArticleCommentResponse> articleCommentsResponse
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
            organizeChildComments(dto.articleCommentDtos())
        );
    }

    private static Set<ArticleCommentResponse> organizeChildComments(Set<ArticleCommentDto> dtos){
        Map<Long, ArticleCommentResponse> map = dtos.stream()
            .map(ArticleCommentResponse::from)
            .collect(Collectors.toMap(ArticleCommentResponse::id, Function.identity()));

        map.values().stream()
            .filter(ArticleCommentResponse::hasParentComment)
            .forEach(comment -> {
                ArticleCommentResponse parentComment = map.get(comment.parentCommentId());
                parentComment.childComments().add(comment);
            });

        return map.values().stream()
            .filter(comment -> !comment.hasParentComment())
            .collect(Collectors.toCollection(() ->
                new TreeSet<>(Comparator
                    .comparing(ArticleCommentResponse::createdAt)
                    .reversed()
                    .thenComparingLong(ArticleCommentResponse::id))));
    }
}
