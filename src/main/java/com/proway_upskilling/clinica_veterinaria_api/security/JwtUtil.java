package com.proway_upskilling.clinica_veterinaria_api.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expiration = 1000 * 60 * 60; // 1 hora


    public String generateToken(String crmv, Long veterinarioId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("crmv", crmv);
        claims.put("veterinario_id", veterinarioId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(crmv)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }


    public boolean isTokenValid(String token) {
        try {
            return !extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractCrmv(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public Long extractVeterinarioId(String token) {
        Claims claims = extractAllClaims(token);
        Object id = claims.get("veterinario_id");
        return id != null ? Long.parseLong(id.toString()) : null;
    }

}