package gymmi.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public final class TokenProcessor {

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

    private String generateToken(Long userId, Long expiredTime) {
        long now = System.currentTimeMillis();
        String jwt = Jwts.builder()
                .header()
                .add("alg", "HS256")
                .type("jwt").and()
                .claims()
                .add("userId", String.valueOf(userId))
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiredTime)).and()
                .signWith(secretKey)
                .compact();
        return jwt;
    }

    public String generateAccessToken(Long userId) {
        return generateToken(userId, accessExpiredTime);
    }

    public String generateRefreshToken(Long userId) {
        return generateToken(userId, refreshExpiredTime);
    }

}
