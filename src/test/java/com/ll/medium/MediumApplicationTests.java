package com.ll.medium;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
//application.yml, application-test.yml 로 실행된다.
//그말은 spring: profiles: active: test 로 변경시켜준다는말??? ㅎGPT는 그렇다고 함
//ActiveProfile는 test일 때만 사용한다.
@ActiveProfiles("test")
class MediumApplicationTests {

    @Test
    void contextLoads() {
    }

}
