package com.fastcampus.projectBoard.controller;

import com.fastcampus.projectBoard.domain.constant.FormStatus;
import com.fastcampus.projectBoard.domain.constant.SearchType;
import com.fastcampus.projectBoard.dto.UserAccountDto;
import com.fastcampus.projectBoard.dto.request.ArticleRequest;
import com.fastcampus.projectBoard.dto.response.ArticleResponse;
import com.fastcampus.projectBoard.dto.response.ArticleWithCommentResponse;
import com.fastcampus.projectBoard.service.ArticleService;
import com.fastcampus.projectBoard.service.PaginationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;

    @GetMapping
    public String articles(
        @RequestParam(required = false, name = "searchType") SearchType searchType,
        @RequestParam(required = false, name = "searchValue") String searchValue,
        @PageableDefault(size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
        ModelMap map
    ) {
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());

        map.addAttribute("articles", articles);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values());
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map) {
        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.getArticleWithComments(articleId));

        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentResponses());
        map.addAttribute("totalCount", articleService.getArticleCount());
        return "articles/detail";
    }

    @GetMapping("/search-hashtag")
    public String searchArticleHashtag(
        @RequestParam(required = false, name = "searchValue") String searchValue,
        @PageableDefault(size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
        ModelMap map
    ){
        Page<ArticleResponse> articles = articleService.searchArticlesViaHashtag(searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        List<String> hashtags = articleService.getHashtags();

        map.addAttribute("articles", articles);
        map.addAttribute("hashtags", hashtags);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchType", SearchType.HASHTAG);

        return "articles/search-hashtag";
    }

    @GetMapping("/form")
    public String articleForm(ModelMap map){
        map.addAttribute("formStatus", FormStatus.CREATE);
        return "articles/form";
    }

    @PostMapping("/form")
    public String postNewArticle(ArticleRequest articleRequest){
        articleService.saveArticle(articleRequest.toDto(UserAccountDto.of(
            "uno", "asdf1234", "uno@mail.com", "Uno", "I am Uno.", null, null, null, null
        )));

        return "redirect:/articles";
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map){
        ArticleResponse article = ArticleResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", article);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "articles/form";
    }

    @PostMapping("/{articleId}/form")
    public String updateArticle(@PathVariable Long articleId, ArticleRequest articleRequest){
        articleService.updateArticle(articleId, articleRequest.toDto(UserAccountDto.of(
            "uno", "asdf1234", "uno@mail.com", "Uno", "I am Uno.", null, null, null, null
        )));

        return "redirect:/articles/" + articleId;
    }

    @PostMapping("/{articleId}/delete")
    public String deleteArticle(@PathVariable Long articleId){
        articleService.deleteArticle(articleId);

        return "redirect:/articles";
    }
}
