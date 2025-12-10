package com.wiss.dragonball.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service stellt Methoden bereit, um JWTs zu erzeugen und zu validieren.
 * Das Secret muss mindestens 32 Zeichen lang sein und wird aus der
 * Umgebungsvariable {@code JWT_SECRET} geladen. Die Ablaufzeit des Tokens
 * wird über {@code jwt.expiration-ms} (Default: 86400000 ms = 24 h) gesteuert.
 * Zusätzlich werden die Rollen des Benutzers als Claim "roles" gespeichert.
 */
@Service
public class JwtService {
    private final Key signingKey;
    private final long jwtExpirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long jwtExpirationMs) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    /** Extrahiert den Benutzernamen (Subject) aus dem Token. */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Extrahiert ein bestimmtes Claim aus dem Token mithilfe der übergebenen Funktion. */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** Erzeugt ein signiertes JWT für einen Benutzer und speichert die Rollen als Claim "roles". */
    public String generateToken(UserDetails userDetails) {
        // Rollen sammeln und als kommaseparierte Liste ablegen
        String roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .claim("roles", roles)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Prüft, ob das Token zum Benutzer gehört und nicht abgelaufen ist. */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
