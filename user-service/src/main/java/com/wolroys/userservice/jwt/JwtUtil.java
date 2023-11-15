package com.wolroys.userservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    public String secret; //TODO move to git configuration

    @Value("${jwt.lifetime}")
    private Integer jwtLifetime;

    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(getSignKey())
                .build().parseSignedClaims(token);
            return true;
        } catch (JwtException e){
            System.out.println("Invalid JWT token: " +  e.getMessage());
            return false;
        }
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roles);
        Date expiration = new Date(jwtLifetime);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(getSignKey()).compact();
    }

    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser().verifyWith(getSignKey())
                .build().parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }
}
