package com.fastcampus.projectBoard.domain;

import java.time.LocalDateTime;

public class Article {
    private Long id;
    private String title; //제목
    private String content; //본문
    private String hashtag; //해시태그

    private LocalDateTime createAt; //생성일시
    private String createBy; //생성자
    private LocalDateTime modifyAt; //수정일시
    private String modifyBy; //수정자
}
