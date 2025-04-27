package com.fastcampus.projectBoard.repository;

import com.fastcampus.projectBoard.domain.UserAccount;
import com.fastcampus.projectBoard.domain.projection.UserAccountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = UserAccountProjection.class)
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

}
