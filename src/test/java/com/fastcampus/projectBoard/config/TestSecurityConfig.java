package com.fastcampus.projectBoard.config;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fastcampus.projectBoard.dto.UserAccountDto;
import com.fastcampus.projectBoard.service.UserAccountService;
import java.util.Optional;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockitoBean
    private UserAccountService userAccountService;

    @BeforeTestMethod
    public void securitySetUp() {
        given(userAccountService.searchUser(anyString()))
            .willReturn(Optional.of(createUserAccount()));
        given(userAccountService.saveUser(anyString(), anyString(), anyString(), anyString(), anyString()))
                .willReturn(createUserAccount());
    }

    private UserAccountDto createUserAccount() {
        return UserAccountDto.of(
            "unoTest",
            "pw",
            "uno-test@email.com",
            "uno-test",
            "test memo"
        );
    }

}
