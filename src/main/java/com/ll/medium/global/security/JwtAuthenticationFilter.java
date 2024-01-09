package com.ll.medium.global.security;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        System.out.println("JwtAuthenticationFilter 실행");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(username != null && password != null){
            Member member = memberService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
            if(!passwordEncoder.matches(password, member.getPassword())){
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            User user = new User(member.getUsername(), member.getPassword(), List.of());

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user,
                    user.getPassword(),
                    user.getAuthorities()
            );
            // 스프링 시큐리티 세션에 해당 객체 저장해서 로그인 처리
            SecurityContextHolder.getContext().setAuthentication(auth);


        }
        filterChain.doFilter(request, response);
    }
}
