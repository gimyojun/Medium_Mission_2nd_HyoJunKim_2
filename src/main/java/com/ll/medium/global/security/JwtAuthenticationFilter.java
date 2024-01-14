package com.ll.medium.global.security;

import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.auth.CustomUser;
import com.ll.medium.global.rq.Rq.Rq;
import com.ll.medium.global.rsData.RsData.RsData;
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
        //로그인과 로그아웃일때는 필터링 하지 않음
        if(request.getRequestURI().equals("/api/v1/members/login") || request.getRequestURI().equals("/api/v1/members/logout")){
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = rq.getCookieValue("accessToken", "");
        String refreshToken = rq.getCookieValue("refreshToken", "");
        if(!accessToken.isBlank()){
            if(!memberService.validateAccessToken(accessToken)){
                RsData<String> rs = memberService.refreshAccessToken(refreshToken);
                accessToken = rs.getData();
                rq.setCrossDomainCookie("accessToken", accessToken);
            }
            CustomUser user = memberService.getUserFromAccessToken(accessToken);
            rq.setAuthentication(user);
        }
        filterChain.doFilter(request, response);
    }
}
