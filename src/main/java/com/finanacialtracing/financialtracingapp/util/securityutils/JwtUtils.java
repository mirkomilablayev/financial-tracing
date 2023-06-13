package com.finanacialtracing.financialtracingapp.util.securityutils;


import com.finanacialtracing.financialtracingapp.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final long expire = 1000*60*60;
    private static final String key = "AqishUsBAsusbaJs)a9s!s_-";

    public String generateToken(String username, User user) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", user.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public Boolean isValidToken(String token){
        try{
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
