package com.swapandgo.sag.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private final SecretKey key;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpirationTime
            ){
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    //Access Token 생성
    public String generateAccessToken(String email){
        return createToken(email, accessTokenExpirationTime);
    }

    //Refresh Token 생성
    public String generateRefreshToken(String email){
        return createToken(email, refreshTokenExpirationTime);
    }


    //JWT 토큰 생성 메서드
    private String createToken(String email, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }



    //JWT 토큰에서 이메일 추출
    public String getEmailFromToken(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key) //이 키로 서명을 검증
                    .build()
                    .parseClaimsJws(token) // 전달된 token 문자열을 header / payload / signature로 분리하고 key를 이용해 서명 검증하고 exp(만료 시간)도 체크하고 이상 없으면 Jws<Claims> 객체를 반환.만료되었으면ExpiredJwtException, 서명이 틀리거나 형식이 이상하면 JwtException 등 예외를 던짐
                    .getBody()//Jws<Claims>에서 payload(Claims) 꺼내는 부분
                    .getSubject(); //payload 안에서 sub 클레임을 꺼냄
        }catch (ExpiredJwtException e){
            throw new JwtException("토큰이 만료되었습니다.");
        }catch (JwtException e){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
            log.warn("만료된 JWT 토큰입니다.");
            return false;
        }catch (JwtException | IllegalArgumentException e){
            log.warn("유효하지 않은 JWT 토큰입니다.");
            return false;
        }
    }


}


