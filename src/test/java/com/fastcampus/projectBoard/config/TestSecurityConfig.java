package com.fastcampus.projectBoard.config;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fastcampus.projectBoard.domain.UserAccount;
import com.fastcampus.projectBoard.repository.UserAccountRepository;
import java.util.Optional;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@Import(SecurityConfig.class)
public class TestSecurityConfig {
    @MockitoBean private UserAccountRepository userAccountRepository;

    @BeforeTestMethod
    public void securitySetup(){
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(UserAccount.of(
            "userTest",
            "pw",
            "6031@email.com",
            "uno-test",
            "test memo"
        )));
    }
}
