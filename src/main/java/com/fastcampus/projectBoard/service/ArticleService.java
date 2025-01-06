package com.fastcampus.projectBoard.service;

import com.fastcampus.projectBoard.domain.type.SearchType;
import com.fastcampus.projectBoard.dto.ArticleDto;
import com.fastcampus.projectBoard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectBoard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Setter
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleDto searchArticles(long l) {
        return null;
    }

    public void saveArticle(ArticleDto dto) {
    }

    public void updateArticle(ArticleDto dto) {
    }

    public void deleteArticle(long articleId) {
    }

    public ArticleWithCommentsDto getArticle(Long articleId) {
        return null;
    }
}
