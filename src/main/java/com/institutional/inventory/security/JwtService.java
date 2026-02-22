package com.institutional.inventory.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // JWT'yi mühürlemek için kriptografik gizli anahtar (En az 256 bit olmalı)
    // Gerçek hayatta bu kodun içinde açıkça yazılmaz, dışarıdan gizlice verilir!
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    // 1. TOKEN ÜRETME METODU (Giriş başarılı olunca çalışacak)
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Kartın sahibi (Kullanıcı adı)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Ne zaman basıldı?
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 Saat geçerli
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Bizim gizli anahtarla şifrele
                .compact(); // Metin (String) haline getir
    }

    // 2. DIŞARIDAN GELEN TOKEN'IN İÇİNDEN İSMİ OKUMA METODU
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 3. TOKEN GEÇERLİ Mİ KONTROLÜ (Süresi dolmuş mu? Başkasına mı ait?)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}