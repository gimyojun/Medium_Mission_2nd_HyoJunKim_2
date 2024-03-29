package com.ll.medium.global.init;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.stream.IntStream;

@Configuration
@Slf4j
//application.yml의 profiles.active가 prod일경우만 실행되는것
@Profile("prod")
@RequiredArgsConstructor
public class Prod {
    @Autowired
    @Lazy
    private Prod self;
    private final MemberService memberService;
    private final PostService postService;

    @Bean
    @Order(3)
    public ApplicationRunner initNotProd() {
        return args -> {
           self.work2();
        };


    }
    
    @Transactional
    public void work2() {
        // 이미 회원이 존재하는 경우 초기화를 수행하지 않음
        if (memberService.findByUsername("user1").isPresent()) return;

        // 유료멤버십 회원 생성
        IntStream.rangeClosed(1, 100).forEach(i -> {
            boolean isPaidMember = i % 2 == 0; // 홀수는 유료, 짝수는 무료
            memberService.join("user" + i, "123", isPaidMember);
        });

        // 유료글 생성
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = memberService.findByUsername("user" + i).orElseThrow();

            boolean isPaidPost = i % 2 == 0; // 홀수는 유료, 짝수는 무료
            boolean isPublished = i % 3 != 0; // 1/3의 확률로 공개글
            postService.write(member, "제목 " + i, "내용 " + i,  isPublished, isPaidPost);
        });
    }


}
