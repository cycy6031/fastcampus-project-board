package com.fastcampus.projectBoard.dto;

import com.fastcampus.projectBoard.domain.Article;
import com.fastcampus.projectBoard.domain.UserAccount;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.fastcampus.projectBoard.domain.Article}
 */
public record ArticleDto(
    Long id,
    UserAccountDto userAccountDto,
    String title,
    String content,
    Set<HashtagDto> hashtagDtos,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime modifiedAt,
    String modifiedBy)
{

    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, Set<HashtagDto> hashtagDtos, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleDto(id, userAccountDto, title, content, hashtagDtos, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleDto of(UserAccountDto userAccountDto, String title, String content, Set<HashtagDto> hashtagDtos){
        return new ArticleDto(null, userAccountDto, title, content, hashtagDtos, null, null, null, null);
    }

    public static ArticleDto from(Article entity) {
        return new ArticleDto(
            entity.getId(),
            UserAccountDto.from(entity.getUserAccount()),
            entity.getTitle(),
            entity.getContent(),
            entity.getHashtags().stream()
                .map(HashtagDto::from)
                .collect(Collectors.toUnmodifiableSet()),
            entity.getCreatedAt(),
            entity.getCreatedBy(),
            entity.getModifiedAt(),
            entity.getModifiedBy()
        );
    }

    public Article toEntity(UserAccount userAccount) {
        return Article.of(
            userAccount,
            title,
            content
        );
    }
}