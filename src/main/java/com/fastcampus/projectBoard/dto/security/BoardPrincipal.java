package com.fastcampus.projectBoard.dto.security;

import com.fastcampus.projectBoard.dto.UserAccountDto;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record BoardPrincipal (
    String username,
    String password,
    Collection<? extends GrantedAuthority> authorities,
    String email,
    String nickname,
    String memo,
    Map<String, Object> oAuth2Attribute
) implements UserDetails, OAuth2User {

    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo) {
        return of(username, password, email, nickname, memo, Map.of());
    }

    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo, Map<String, Object> oAuth2Attribute) {
        Set<RoleType> roleTypes = Set.of(RoleType.User);

        return new BoardPrincipal(
            username,
            password,
            roleTypes.stream()
                .map(RoleType::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet()),
            email,
            nickname,
            memo,
            oAuth2Attribute
        );
    }

    public static BoardPrincipal from(UserAccountDto dto){
        return BoardPrincipal.of(
            dto.userId(),
            dto.userPassword(),
            dto.email(),
            dto.nickname(),
            dto.memo()
        );
    }

    public UserAccountDto toDto(){
        return UserAccountDto.of(
            username,
            password,
            email,
            nickname,
            memo
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return username;
    }

    @Override
    public String getUsername() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public Map<String, Object> getAttributes(){
        return oAuth2Attribute;
    }

    @Override
    public String getName(){
        return username;
    }

    private enum RoleType {
        User("ROLE_USER");

       @Getter
       private final String name;

       RoleType(String name){
           this.name = name;
       }
    }
}
