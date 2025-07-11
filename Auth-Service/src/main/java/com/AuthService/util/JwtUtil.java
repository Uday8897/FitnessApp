package com.AuthService.util;

import com.AuthService.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private Key secretKey;

    JwtUtil() {
        byte[] keyBytes = Base64.getDecoder().decode("nLzlhkkvOLkQ1A1xN9aI9qXZBJ2n5b0QaQfqTKxw51I=");
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .signWith(secretKey)
                .setSubject(user.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .compact();
    }

    public boolean verifyToken(String token) {
        try{
            Object body = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parse(token)
                    .getBody();
            System.out.println(body.toString());
            return true;
        }catch (Exception e){
            return false;

        }
    }
}
