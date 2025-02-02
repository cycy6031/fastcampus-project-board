package com.fastcampus.projectBoard.service;

import com.fastcampus.projectBoard.domain.UserAccount;
import com.fastcampus.projectBoard.dto.UserAccountDto;
import com.fastcampus.projectBoard.repository.UserAccountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public Optional<UserAccountDto> searchUser(String username) {
        return userAccountRepository.findById(username)
            .map(UserAccountDto::from);
    }

    public UserAccountDto saveUser(String username, String password, String email, String nickname, String memo) {
        return UserAccountDto.from(
            userAccountRepository.save(
                UserAccount.of(username, password, email, nickname, memo, username))
        );
    }

}
