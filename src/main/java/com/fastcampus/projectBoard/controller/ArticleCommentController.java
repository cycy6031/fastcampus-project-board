package com.fastcampus.projectBoard.controller;

import com.fastcampus.projectBoard.dto.UserAccountDto;
import com.fastcampus.projectBoard.request.ArticleCommentRequest;
import com.fastcampus.projectBoard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest){

        articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDto.of(
            "uno", "asdf1234", "uno@mail.com", "Uno", "I am Uno"
        )));

        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId, Long articleId){

        articleCommentService.deleteArticleComment(commentId);

        return "redirect:/articles/" + articleId;
    }

}
