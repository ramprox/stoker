package ru.stoker.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JwtService {

    private final String key;

    private static final String LOGIN = "login";

    public JwtService(@Value("${security.jwtKey}") String key) {
        this.key = key;
    }

    public String generateToken(String login) {
        return Jwts.builder()
                .addClaims(Map.of(LOGIN, login))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getLoginFromToken(String token) {
        Claims body = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return body.get(LOGIN, String.class);
    }

}
