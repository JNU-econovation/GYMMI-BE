package gymmi.service;

import gymmi.exceptionhandler.exception.AuthenticationFailException;
import gymmi.exceptionhandler.message.LoginErrorMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public final class TokenProcessor {

    private static final String CLAIM_KEY_USER_ID = "userId";
    private static final String SUBJECT_VALUE_ACCESS_TOKEN = "AT";
    private static final String SUBJECT_VALUE_REFRESH_TOKEN = "RT";
    private final SecretKey secretKey;
    private final Long accessExpiredTime;
    private final Long refreshExpiredTime;

    public TokenProcessor(
            @Value("${jwt.token.secret-key}") String secretKey,
            @Value("${jwt.token.access-expiration-time}") Long accessExpiredTime,
            @Value("${jwt.token.refresh-expiration-time}") Long refreshExpiredTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessExpiredTime = accessExpiredTime;
        this.refreshExpiredTime = refreshExpiredTime;
    }

    private String generateToken(Long userId, Long expiredTime, String subject) {
        long now = System.currentTimeMillis();
        String jwt = Jwts.builder()
                .header()
                .add("alg", "HS256")
                .type("jwt").and()
                .claims()
                .add(CLAIM_KEY_USER_ID, String.valueOf(userId))
                .subject(subject)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiredTime)).and()
                .signWith(secretKey)
                .compact();
        return jwt;
    }

    public String generateAccessToken(Long userId) {
        return generateToken(userId, accessExpiredTime, SUBJECT_VALUE_ACCESS_TOKEN);
    }

    public String generateRefreshToken(Long userId) {
        return generateToken(userId, refreshExpiredTime, SUBJECT_VALUE_REFRESH_TOKEN);
    }

    public Long parseAccessToken(String accessToken) {
        return parseToken(accessToken, SUBJECT_VALUE_ACCESS_TOKEN);
    }

    public Long parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, SUBJECT_VALUE_REFRESH_TOKEN);
    }

    private Long parseToken(String token, String subject) {
        try {
            Claims payload = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            if (!payload.getSubject().equals(subject)) {
                throw new AuthenticationFailException(LoginErrorMessage.NOT_MATCHED_JWT_SUBJECT);
            }
            String userId = payload
                    .get(CLAIM_KEY_USER_ID, String.class);
            return Long.valueOf(userId);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationFailException(LoginErrorMessage.EXPIRED_JWT.getMessage(), e);
        } catch (JwtException e) {
            throw new AuthenticationFailException(LoginErrorMessage.JWT_RELATED_ERROR.getMessage(), e);
        }
    }

}

