package com.ll.medium.global.init;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.stream.IntStream;

@Configuration
@Profile("!prod")
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    private final MemberService memberService;
    private final PostService postService;

    @Bean
    @Order(3)
    public ApplicationRunner initNotProd() {
        return args -> {
            if (memberService.findByUsername("user1").isPresent()) return;

            Member memberUser1 = memberService.join("user1", "123",true).getData();
            Member memberUser2 = memberService.join("user2", "123",false).getData();
            Member memberUser3 = memberService.join("user3", "123",false).getData();


            postService.write(memberUser1, "유료 제목 1", "유료 내용 1", true,true);
            postService.write(memberUser1, "유료 제목 2", "유료 내용 2", false,true);


            postService.write(memberUser2, "제목 3", "내용 3", true,false);
            postService.write(memberUser2, "제목 4", "내용 4", false,false);

            IntStream.rangeClosed(5, 50).forEach(i -> {
                postService.write(memberUser3, "제목 " + i, "내용 " + i, true,false);
            });
        };
    }
}
