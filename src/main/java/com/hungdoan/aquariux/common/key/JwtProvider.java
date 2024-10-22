package com.hungdoan.aquariux.common.key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    private final PrivateKey privateKey;

    private final PublicKey publicKey;

    @Autowired
    public JwtProvider(KeyLoader keyLoader) {
        this.privateKey = keyLoader.getPrivateKey();
        this.publicKey = keyLoader.getPublicKey();
    }

    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        SimpleGrantedAuthority userId = (SimpleGrantedAuthority) user.getAuthorities().iterator().next();
        SimpleGrantedAuthority userRole = (SimpleGrantedAuthority) user.getAuthorities().iterator().next();
        claims.put("id", userId.getAuthority());
        claims.put("username", user.getUsername());
        claims.put("role", userRole.getAuthority());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(5).toInstant()))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
        return claims.get("username", String.class);
    }
}

