package com.ll.medium.global.auth;

import com.ll.medium.domain.member.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

//Authentication 객체에 저장할수있는 유일한 타입
public class CustomUserDetails implements UserDetails, OAuth2User {

    private Member member;
    private Map<String, Object> attributes;

    //일반로그인시 사용
    public CustomUserDetails(Member member){
        this.member=member;
    }
    //OAuth2 로그인시 사용
    public CustomUserDetails(Member member, Map<String, Object> attributes){
        this.member = member;
        this.attributes = attributes;
    }
    //UserDetails구현 시작
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getAuthorities();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
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
        return attributes;
    }

    // User의 PrimaryKey
    @Override
    public String getName() {
        return member.getId()+"";
    }
    //OAuth2User구현 끝

}
