package com.ll.medium.domain.member.member.controller;

import com.ll.medium.domain.member.member.dto.MemberDto;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rq.Rq.Rq;
import com.ll.medium.global.rsData.RsData.RsData;
import com.ll.medium.global.util.jwt.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MembersController {
    private final MemberService memberService;
    private final Rq rq;

    @Getter
    @Setter
    public static class LoginRequestBody {
        private String username;
        private String password;

    }
    @Getter
    public static class LoginResponseBody {
        private final MemberDto result;
        private final String accesToken;
        private final String refreshToken;

        public LoginResponseBody(Member member, String accesToken, String refreshToken) {
            result = new MemberDto(member);
            this.accesToken = accesToken;
            this.refreshToken = refreshToken;
        }

    }

    @PostMapping("/login")
    public RsData<LoginResponseBody> login(@RequestBody LoginRequestBody requestBody) {
        RsData<Member> rsData = memberService.checkUsernameAndPassword(requestBody.getUsername(), requestBody.getPassword());
        Member member = rsData.getData();
        Long id = member.getId();
        String accessToken = JwtUtil.encode(
                Map.of(
                        "id", id.toString(),
                        "username", member.getUsername(),
                        "authorities", member.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList())
                ),
                (long) 1000 * 60 * 10
        );
        String refreshToken = JwtUtil.encode(
                Map.of(
                        "id", id.toString(),
                        "username", member.getUsername()

                ),
                (long) 1000 * 60 * 60 * 24 * 365
        );

        memberService.setRefreshToken(member, refreshToken);
        // return new RsData<LoginResponseBody>
        return rsData.of(new LoginResponseBody(member, accessToken, refreshToken));
    }
    @Getter
    @Setter
    public static class RefreshAccessTokenRequestBody {
        private String refreshToken;
    }
    @Getter
    public static class RefreshAccessTokenResponseBody {
        private final String accessToken;
        public RefreshAccessTokenResponseBody(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    @PostMapping("/refreshAccessToken")
    public RsData<RefreshAccessTokenResponseBody> refreshAccessToken(@RequestBody RefreshAccessTokenRequestBody requestBody) {
        String refreshToken = requestBody.getRefreshToken();
        Member member = memberService.findByRefreshToken(refreshToken).get();
        Long id = member.getId();
        String accessToken = JwtUtil.encode(
                Map.of(
                        "id", id.toString(),
                        "username", member.getUsername(),
                        "authorities", member.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList())
                ),
                (long) 1000 * 60 * 10
        );
        return RsData.of( "200",
                "엑세스 토큰 재발급 성공",
                new RefreshAccessTokenResponseBody(accessToken)
        );
    }


}
