package com.ll.medium.global.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

//User 클래스는 UserDetails를 구현한 대표적인 클래스 중 하나
//CustomUser는 UserDetails를 구현한 커스텀 클래스
//Authentication 객체에 저장할수있는 유일한 타입
public class CustomUser implements UserDetails, OAuth2User {

    @Getter
    private long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private Map<String, Object> attributes;

    //일반로그인시 사용
    public CustomUser(long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    //OAuth2 로그인시 사용
    public CustomUser(long id, String username, String password, Collection<? extends GrantedAuthority> authorities,Map<String, Object> attributes){
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.attributes = attributes;
    }



    //UserDetails구현 시작
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
    //UserDetails구현 끝

    //OAuth2User구현 시작
    // 리소스 서버로 부터 받는 회원정보
    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.username;
    }

    // User의 PrimaryKey

    //OAuth2User구현 끝

}
