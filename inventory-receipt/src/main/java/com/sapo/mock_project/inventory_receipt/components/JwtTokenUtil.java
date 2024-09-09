package com.sapo.mock_project.inventory_receipt.components;

import com.sapo.mock_project.inventory_receipt.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${token.expiration}")
    private int expiration;

    @Value("${token.secretKey}")
    private String secretKey;

    @Value("${token.expiration-refresh-token}")
    private int expirationRefreshToken;

    @Value("${token.type}")
    private String tokenType;

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    public String getTokenType() {
        return tokenType;
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());

        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(String.valueOf(user.getId()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();

            return token;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());

        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(String.valueOf(user.getId()))
                    .setExpiration(new Date(System.currentTimeMillis() + expirationRefreshToken * 1000L))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();

            return token;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private Key getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);

        return expirationDate.before(new Date());
    }

    public Long extractId(String token) {
        return Long.valueOf(extractClaim(token, Claims::getSubject));
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}

