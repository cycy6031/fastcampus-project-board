package com.fastcampus.projectBoard.config;

import com.fastcampus.projectBoard.domain.Article;
import com.fastcampus.projectBoard.domain.ArticleComment;
import com.fastcampus.projectBoard.domain.Hashtag;
import com.fastcampus.projectBoard.domain.UserAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class DataRestConfig {

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer(){
        return RepositoryRestConfigurer.withConfig((config, cors) ->
            config
                .exposeIdsFor(UserAccount.class)
                .exposeIdsFor(Article.class)
                .exposeIdsFor(ArticleComment.class)
                .exposeIdsFor(Hashtag.class)
        );
    }

}
