package com.ll.medium.global.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET_KEY = "mediumabcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890";

    public static String encode(Map<String, Object> data, long expireTime) {
        Claims claims = Jwts
                .claims()
                .setSubject("medium jwt token")
                .add("data", data)
                .build();

        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireTime); // 1초(1000)  1분(60)  1시간(60)  1일(24) * 7 = 7일
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Claims decode(String token) {
        return Jwts
                .parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getPayload();


    }
}
