package com.fastcampus.projectBoard.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fastcampus.projectBoard.config.SecurityConfig;
import com.fastcampus.projectBoard.service.ArticleCommentService;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleCommentController.class)
class ArticleCommentControllerTest {

    private final MockMvc mvc;

    @MockitoBean private ArticleCommentService articleCommentService;
    //@MockitoBean private

    public ArticleCommentControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

}