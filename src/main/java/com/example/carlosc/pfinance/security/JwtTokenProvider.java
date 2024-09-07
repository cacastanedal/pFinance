package com.example.carlosc.pfinance.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

  private final String jwtSecret = "I'm the best"; // Use a secure key
  private final long jwtExpirationInMs = 86400000; // 1 day

  public String generateToken(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    return Jwts.builder()
      .setSubject(userDetails.getUsername())
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
      .signWith(SignatureAlgorithm.HS512, jwtSecret)
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser()
      .setSigningKey(jwtSecret)
      .parseClaimsJws(token)
      .getBody();
    return claims.getSubject();
  }
}
