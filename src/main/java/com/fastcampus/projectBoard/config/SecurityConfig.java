package com.fastcampus.projectBoard.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.fastcampus.projectBoard.dto.UserAccountDto;
import com.fastcampus.projectBoard.dto.security.BoardPrincipal;
import com.fastcampus.projectBoard.dto.security.KakaoOAuth2Response;
import com.fastcampus.projectBoard.repository.UserAccountRepository;
import com.fastcampus.projectBoard.service.UserAccountService;
import java.util.UUID;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService
    ) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/",
                    "/articles",
                    "/articles/search-hashtag"
                    ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(withDefaults())
            .logout(logout -> logout
                .logoutSuccessUrl("/")
            )
            .oauth2Login(oAuth -> oAuth
                .userInfoEndpoint(userInfo ->userInfo
                    .userService(null)
                )
            )
        ;
        /*http
            .csrf(AbstractHttpConfigurer::disable);
        http
            .authorizeHttpRequests(
                authorize -> authorize
                    .requestMatchers(HttpMethod.GET).permitAll()
                    .requestMatchers("/articles").permitAll()
                    .requestMatchers("/articles/search-hashtag").permitAll()
                    .anyRequest().authenticated()
            );
        http
            .formLogin( form -> form
                .loginPage("/"));
        http
            .logout((logout) -> logout
                    .logoutSuccessUrl("/"));*/
            /*.authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/articles",
                    "/articles/search-hashtag"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin().and()
            .logout()
            .logoutSuccessUrl("/")
            .and()
            .build();*/

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository
            .findById(username)
            .map(UserAccountDto::from)
            .map(BoardPrincipal::from)
            .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
        UserAccountService userAccountService,
        PasswordEncoder passwordEncoder
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String providerId = String.valueOf(kakaoResponse.id());
            String username = registrationId + "_" + providerId;
            String dummyPassword = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID());

            return userAccountService.searchUser(username)
                .map(BoardPrincipal::from)
                .orElseGet(() ->
                    BoardPrincipal.from(
                        userAccountService.saveUser(
                            username,
                            dummyPassword,
                            kakaoResponse.email(),
                            kakaoResponse.nickname(),
                            null
                        )
                    )
                );
        };

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
