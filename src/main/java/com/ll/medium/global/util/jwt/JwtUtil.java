package com.ll.medium.global.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    public static String encode(Map<String, String> data) {
        Claims claims = Jwts
                .claims()
                .setSubject("medium jwt token")
                .add("type", "access_token")
                .add("data", data)
                .build();

        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000 * 60 * 60 * 24 * 7); // 1초  1분  1시간  1일 * 7 = 7일
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, "mediumabcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890")
                .compact();
    }
}
