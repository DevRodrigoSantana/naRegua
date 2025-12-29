package br.com.naregua.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JWTUtils {

    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";

    private final Key key;

    public JWTUtils(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Date calculateExpiration(long minutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = now.plusMinutes(minutes);
        return Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant());
    }

    public JWTToken createAccessToken(UUID id, String email, String role, long time) {
        Date issuedAt = new Date();
        Date expiration = calculateExpiration(time);

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .claim("id", id.toString())
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new JWTToken(token, time);
    }

    public JWTToken createRefreshToken(UUID id, String email, long time) {
        Date issuedAt = new Date();
        Date expiration = calculateExpiration(time);

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .claim("id", id.toString())
                .claim("role", "REFRESH_TOKEN")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new JWTToken(token, time);
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refactorToken(token))
                    .getBody();
        } catch (JwtException ex) {
            log.error("Token inválido: {}", ex.getMessage());
            return null;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    public String getIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.get("id", String.class) : null;
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refactorToken(token));
            return true;
        } catch (JwtException ex) {
            log.error("Token inválido ou expirado: {}", ex.getMessage());
            return false;
        }
    }

    private String refactorToken(String token) {
        if (token.startsWith(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }
}
