package com.fastcampus.projectBoard.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.fastcampus.projectBoard.domain.Article;
import com.fastcampus.projectBoard.domain.ArticleComment;
import com.fastcampus.projectBoard.domain.UserAccount;
import com.fastcampus.projectBoard.domain.type.SearchType;
import com.fastcampus.projectBoard.dto.ArticleCommentDto;
import com.fastcampus.projectBoard.dto.ArticleDto;
import com.fastcampus.projectBoard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectBoard.dto.UserAccountDto;
import com.fastcampus.projectBoard.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks private ArticleService sut;

    @Mock private ArticleRepository articleRepository;

    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchArticles_thenReturnArticlePage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);
    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환 한다.")
    @Test
    void givenSearchParameters_whenSearchArticles_thenReturnArticlePage() {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitle(searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitle(searchKeyword, pageable);
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchArticles_thenReturnArticleList() {
        // Given
        Long articleId = 1L;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        ArticleWithCommentsDto dto = sut.getArticle(articleId);

        // Then
        assertThat(dto)
            .hasFieldOrPropertyWithValue("title", article.getTitle())
            .hasFieldOrPropertyWithValue("content", article.getContent())
            .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
        then(articleRepository).should().findById(articleId);
    }

    void givenNonexistentArticleId_whenSearchingArticle_thenThrowException() {
        // Given
        Long articleId = 0L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.getArticle(articleId));

        // Then
        assertThat(t)
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("게시글이 없습니다. - articleId : " + articleId);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSaveArticle() {
        // Given
        ArticleDto dto = createArticleDto();
        given(articleRepository.save(any(Article.class))).willReturn(null);

        // When
        sut.saveArticle(dto);

        // Then
        then(articleRepository).should().save(any(Article.class));

    }

    @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springBoot");
        given(articleRepository.getReferenceById(article.getId())).willReturn(article);

        // When
        sut.updateArticle(dto);

        // Then
        assertThat(article)
            .hasFieldOrPropertyWithValue("title", dto.title())
            .hasFieldOrPropertyWithValue("content", dto.content())
            .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
        then(articleRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무것도 하지 않는다.")
    @Test
    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoseNothing() {
        // Given
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springBoot");
        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticle(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        Long articleId = 1L;
        willDoNothing().given(articleRepository).deleteById(articleId);

        // When
        sut.deleteArticle(articleId);

        // Then
        then(articleRepository).should().deleteById(articleId);

    }


    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content", "#java");
    }

    private ArticleDto createArticleDto(String title, String content, String hashtag) {
        return ArticleDto.of(1L,
            createUserAccountDto(),
            title,
            content,
            hashtag,
            LocalDateTime.now(),
            "6031",
            LocalDateTime.now(),
            "6031"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
            1L,
            "6031",
            "password",
            "6031@email.com",
            "6031",
            "this is memo",
            LocalDateTime.now(),
            "6031",
            LocalDateTime.now(),
            "6031"
        );
    }

    private Article createArticle() {
        return Article.of(createUserAccount(), "title", "content", "hashtag");
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
            "6031",
            "password",
            "6031@mail.com",
            "6031",
            null
        );
    }
}