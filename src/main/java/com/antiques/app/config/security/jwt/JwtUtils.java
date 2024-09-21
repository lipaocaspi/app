package com.antiques.app.config.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    @SuppressWarnings("deprecation")
    public String generateAccesToken( String username){
        return Jwts.builder()
            .setSubject( username )
            .setIssuedAt( new Date( System.currentTimeMillis()))
            .setExpiration( new Date( System.currentTimeMillis() + Long.parseLong(timeExpiration.trim())))
            .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    @SuppressWarnings("deprecation")
    public Boolean isTokenValid(String token){
        System.out.println(getSignatureKey());
        try {
            Claims theToken = Jwts.parser()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
            
            System.out.println("CLAIMS: " + theToken);
            return true;
        } catch (Exception e) {
            System.out.println("Token invalido: " + e.getMessage() + token + " ----> " + e.getLocalizedMessage());
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public Claims extractAllClaims(String token){
        return Jwts.parser()
        .setSigningKey(getSignatureKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    public String getUsernameFromUser(String token){
        return  this.getClaim(token, Claims::getSubject );
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsFunction){
        Claims  claims = extractAllClaims(token);
        return claimsFunction.apply(claims);    
    }

    public Key getSignatureKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes); 
    }
}