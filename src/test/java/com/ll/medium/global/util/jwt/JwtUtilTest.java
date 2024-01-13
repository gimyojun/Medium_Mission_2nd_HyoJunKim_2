package com.ll.medium.global.util.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtUtilTest {
    @Test
    @DisplayName("t1")
    void t1() {
        Map<String, Object> map = Map.of(
                "name", "김효준",
                "age", "30"
        );
        String jwtToken = JwtUtil.encode(map, 1000 * 60 * 60);
        System.out.println(jwtToken);
        assertThat(jwtToken).isNotNull();

    }
}
