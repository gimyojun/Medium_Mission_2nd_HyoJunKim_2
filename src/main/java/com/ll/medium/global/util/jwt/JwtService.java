package com.ll.medium.global.util.jwt;

import com.ll.medium.global.app.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
@Service
public class JwtService {

    public String encode(Map<String, Object> data, long expireTime) {
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
                .signWith(SignatureAlgorithm.HS256, AppConfig.getJwtSecretKey())
                .compact();
    }

    public Claims decode(String token) {
        return Jwts
                .parser()
                .setSigningKey(AppConfig.getJwtSecretKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();


    }
    public boolean validateAccessToken(String token) {
        try {
            Claims claims = Jwts
                    .parser()
                    .setSigningKey(AppConfig.getJwtSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // 만료 시간 체크
            if (claims.getExpiration().before(new Date())) {
                return false; // 토큰이 만료됨
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
