package com.fastcampus.projectBoard.repository;

import com.fastcampus.projectBoard.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

}
