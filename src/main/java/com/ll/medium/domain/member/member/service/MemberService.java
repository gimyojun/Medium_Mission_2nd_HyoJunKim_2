package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.global.auth.CustomUser;
import com.ll.medium.global.rsData.RsData.RsData;
import com.ll.medium.global.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> join(String username, String password, boolean isPaid) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("400-2", "이미 존재하는 회원입니다.");
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .isPaid(isPaid)
                .build();
        memberRepository.save(member);

        return RsData.of("200", "%s님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.".formatted(member.getUsername()), member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public long count() {
        return memberRepository.count();
    }

    public Optional<Member> findById(Long l) {
        return memberRepository.findById(l);
    }

    public RsData<Member> checkUsernameAndPassword(String username, String password) {

        Optional<Member> op = memberRepository.findByUsername(username);
        if (op.isPresent()) {
            Member member = op.get();
            if (passwordEncoder.matches(password, member.getPassword())) {
                return RsData.of("200", "success", member);
            }else {
                return RsData.of("400-1", "비밀번호가 일치하지 않습니다.");
            }
        }else {
            return RsData.of("400-2", "존재하지 않는 회원입니다.");
        }
    }

    //쿼리 하나줄임.
    public CustomUser getUserFromAccessToken(String accessToken) {
        Claims claims = JwtUtil.decode(accessToken);

        Map<String, Object> data = (Map<String, Object>) claims.get("data");
        long id = Long.parseLong((String) data.get("id"));

        String username = (String) data.get("username");

        List<? extends GrantedAuthority> authorities = ((List<?>) data.get("authorities"))
                .stream()
                .map(String::valueOf) // Convert each element to String
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new CustomUser(id, username,"", authorities);

    }
    @Transactional
    public void setRefreshToken(Member member, String refreshToken) {
        member.setRefreshToken(refreshToken);
    }


    public Optional<Member> findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken);
    }
}
