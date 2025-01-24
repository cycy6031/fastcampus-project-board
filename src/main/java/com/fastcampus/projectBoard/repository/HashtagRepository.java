package com.fastcampus.projectBoard.repository;

import com.fastcampus.projectBoard.domain.Hashtag;
import com.fastcampus.projectBoard.repository.querydsl.HashtagRepositoryCustom;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface HashtagRepository extends
    JpaRepository<Hashtag, Long>,
    HashtagRepositoryCustom,
    QuerydslPredicateExecutor<Hashtag>
{
    Optional<Hashtag> findByHashtagName(String hashtagName);
    List<Hashtag> findByHashtagNameIn(Set<String> hashtagNames);
}
