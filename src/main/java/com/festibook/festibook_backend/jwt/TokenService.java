package com.festibook.festibook_backend.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class TokenService {
    private Key secretKey;

    @Value("${jwt.token.access-expire-length}")
    private Long ACCESS_EXPIRE_LENGTH;

    @Value("${jwt.token.refresh-expire-length}")
    private Long REFRESH_EXPIRE_LENGTH;

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 자동으로 강력한 키를 생성합니다.
    }

    public String generateAccessToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(userId.toString());
        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE_LENGTH))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken() {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE_LENGTH))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String reGenerateAccessToken(HttpServletRequest request) { // 액세스 토큰 재발급
        validateRefreshToken(request);
        Long id = getUserIdFromToken(request);
        return generateAccessToken(id);
    }

    public String resolveAccessToken(HttpServletRequest request) {
        try {
            String header = request.getHeader("AUTHORIZATION");
            return header.substring("Bearer ".length());
        } catch (Exception e) {
            throw new JwtException("유효하지 않은 토큰");
        }
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        try {
            return request.getHeader("REFRESH-TOKEN");
        } catch (Exception e) {
            throw new JwtException("유효하지 않은 토큰");
        }
    }

    public void validateAccessToken(HttpServletRequest request) { // 만료 여부 검사
        try {
            String token = resolveAccessToken(request);
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new JwtException("토큰 만료");
        }
    }

    public void validateRefreshToken(HttpServletRequest request) {
        try {
            String token = resolveRefreshToken(request);
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) { // 토큰이 만료된 경우
            throw new JwtException("토큰 만료");
        } catch (IllegalArgumentException e) { // 토큰이 비어있거나 형식이 잘못된 경우
            throw new JwtException("유효하지 않은 토큰");
        }
    }

    public Long getUserIdFromToken(HttpServletRequest request) { // 토큰에서 userId 정보 꺼내기
        String token = resolveAccessToken(request);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException e) {
            return Long.parseLong(e.getClaims().getSubject());
        }
    }
}