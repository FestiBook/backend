package com.festibook.festibook_backend.jwt.service;

import com.festibook.festibook_backend.jwt.entity.RefreshToken;
import com.festibook.festibook_backend.jwt.repository.RefreshTokenRepository;
import com.festibook.festibook_backend.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private Key secretKey;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.token.access-expire-length}")
    private Long ACCESS_EXPIRE_LENGTH;

    @Value("${jwt.token.refresh-expire-length}")
    private Long REFRESH_EXPIRE_LENGTH;

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // 액세스 토큰 생성
    public String generateAccessToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(userId.toString());
        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE_LENGTH))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 리프레시 토큰 생성 (DB에 저장)
    public String generateRefreshToken(User user) {
        String token = Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE_LENGTH))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // 토큰을 DB에 저장
        RefreshToken refreshToken = new RefreshToken(token, user, new Date(System.currentTimeMillis() + REFRESH_EXPIRE_LENGTH));
        refreshTokenRepository.save(refreshToken);

        return token;
    }

    public String reGenerateAccessToken(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "유효하지 않은 Refresh 토큰입니다."));

        if (refreshToken.getExpiryDate().before(new Date())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다.");
        }

        return generateAccessToken(refreshToken.getUser().getId());
    }

    public void invalidateRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public String resolveAccessToken(HttpServletRequest request) {
        try {
            String header = request.getHeader("Authorization");
            return header.substring("Bearer ".length());
        } catch (Exception e) {
            throw new JwtException("유효하지 않은 토큰");
        }
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        try {
            return request.getHeader("Refresh-Token");
        } catch (Exception e) {
            throw new JwtException("유효하지 않은 Refresh 토큰입니다.");
        }
    }

    public void validateAccessToken(HttpServletRequest request) {
        try {
            String token = resolveAccessToken(request);
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access 토큰이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "유효하지 않은 Access 토큰입니다.");
        }
    }

    public void validateRefreshToken(HttpServletRequest request) {
        try {
            String token = resolveRefreshToken(request);
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh 토큰이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "유효하지 않은 Refresh 토큰입니다.");
        }
    }

    public Long getUserIdFromToken(HttpServletRequest request) {
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
