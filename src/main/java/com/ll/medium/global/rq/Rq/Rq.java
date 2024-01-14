package com.ll.medium.global.rq.Rq;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.auth.CustomUser;
import com.ll.medium.global.rsData.RsData.RsData;
import com.ll.medium.standard.util.Ut.Ut;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import jakarta.servlet.http.Cookie;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final MemberService memberService;
    private Member member;
    private final EntityManager entityManager;
    private CustomUser customUser;


    public String redirect(String url, String msg) {
        if(msg == null){
            return "redirect:" + url;
        }
        boolean containsTtl = msg.contains(";ttl=");

        if (containsTtl) {
            msg = msg.split(";ttl=", 2)[0];
        }

        msg = URLEncoder.encode(msg, StandardCharsets.UTF_8);
        msg += ";ttl=" + (new Date().getTime() + 1000 * 5);

        return "redirect:" + url + "?msg=" + msg;
    }
    public String historyBack(String msg) {
        request.setAttribute("failMsg", msg);

        return "global/js";
    }

    public String redirectOrBack(RsData<?> rs, String path) {
        if (rs.isFail()) return historyBack(rs.getMsg());

        return redirect(path, rs.getMsg());
    }

    public CustomUser getCustomUser() {
        if (customUser == null) {
            customUser = Optional.ofNullable(SecurityContextHolder.getContext())
                    .map(SecurityContext::getAuthentication)
                    .filter(authentication -> authentication.getPrincipal() instanceof CustomUser)
                    .map(authentication -> (CustomUser) authentication.getPrincipal())
                    .orElse(null);
        }
        return customUser;
    }

    public boolean isLogin() {
        return getCustomUser() != null;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public void setAttribute(String key, Object value) {
        request.setAttribute(key, value);
    }

    public String getCurrentQueryStringWithoutParam(String paramName) {
        String queryString = request.getQueryString();

        if (queryString == null) {
            return "";
        }

        queryString = Ut.url.deleteQueryParam(queryString, paramName);

        return queryString;
    }

    //현재 로그인된 멤버 이름을 통해 객체를 반환
    public Member getLoginedMember(){
        if (isLogout())
            return null;
        Member member = memberService.findByUsername(this.getCustomUser().getUsername()).get();
        return member;
    }

    public Member getAuthenticatedMemberFromSecurityContext(){
        if (isLogout())
            throw new RuntimeException("rq : 로그인이 필요합니다. 로그인 후 다시 시도해주세요.");
        if(member == null)
            member = entityManager.getReference(Member.class, getCustomUser().getId());
        return member;
    }
    public boolean isAdmin(){
        if(isLogout())
            return false;
        return getCustomUser()
                .getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
    public boolean isPaid(){
        if(isLogout())
            return false;
        return getCustomUser()
                .getAuthorities()
                .stream()
                .anyMatch(it -> it.getAuthority().equals("ROLE_PAID"));
    }

    public void setAuthentication(CustomUser user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );
        // 스프링 시큐리티 세션에 해당 객체 저장해서 로그인 처리
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public String getHeader(String name, String defaultValue) {
        String value = request.getHeader(name);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    // 일반
    public boolean isAjax() {
        if ("application/json".equals(request.getHeader("Accept"))) return true;
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    // 쿠키 관련
    public void setCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void setCrossDomainCookie(String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public Cookie getCookie(String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }

        return null;
    }

    public String getCookieValue(String name, String defaultValue) {
        Cookie cookie = getCookie(name);

        if (cookie == null) {
            return defaultValue;
        }

        return cookie.getValue();
    }

    private long getCookieAsLong(String name, int defaultValue) {
        String value = getCookieValue(name, null);

        if (value == null) {
            return defaultValue;
        }

        return Long.parseLong(value);
    }

    public void removeCookie(String name) {
        Cookie cookie = getCookie(name);

        if (cookie == null) {
            return;
        }

        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        ResponseCookie responseCookie = ResponseCookie.from(name, null)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());
    }
}
