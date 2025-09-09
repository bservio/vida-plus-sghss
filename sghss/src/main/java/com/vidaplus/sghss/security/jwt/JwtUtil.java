package com.vidaplus.sghss.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
  private final String SECRET = "uma_chave_hyper_mega_segura_com_mais_de_32_+Caracteres";
  private final long EXPIRATION = 1000*60*60;

  private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

  public String gerarToken(String email) {
    return Jwts.builder()
              .setSubject(email)
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
              .signWith(key, SignatureAlgorithm.HS256)
              .compact();
  }

  public String extrairEmail(String token) {
    return Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody()
              .getSubject();
  }

  public boolean validarToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }


}
