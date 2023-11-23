package com.wolroys.shopgateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Integer lifetime;

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

    public Claims getClaims(String token){
        return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean isExpired(String token){
        try{
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e){
            return false;
        }
    }
}
