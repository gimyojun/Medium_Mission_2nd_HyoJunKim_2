package com.ll.medium.global.security;

import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.auth.CustomUser;
import com.ll.medium.global.rq.Rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final MemberService memberService;
    private final Rq rq;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        System.out.println("JwtAuthenticationFilter 실행");
        // X 붙여주는게 관례다.
        String apiKey = rq.getHeader("X-ApiKey", null);

        String accessToken = rq.getCookieValue("accessToken", "");
        if(!accessToken.isBlank()){
            String refreshToken = rq.getCookieValue("refreshToken", "");
            CustomUser user = memberService.getUserFromAccessToken(accessToken);
            rq.setAuthentication(user);
        }
        filterChain.doFilter(request, response);
    }
}
