package com.institutional.inventory.controller;

import com.institutional.inventory.entity.User;
import com.institutional.inventory.repository.UserRepository;
import com.institutional.inventory.security.JwtService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // 1. KAYIT OL (REGISTER)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        // Eğer bu isimde biri varsa hata dön
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Bu kullanıcı adı zaten alınmış!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        // Şifreyi asla açık metin kaydetmiyoruz! BCrypt ile şifreleyip (Hash) kaydediyoruz.
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER"); // Varsayılan yetki

        userRepository.save(user);

        // Kayıt başarılıysa adama hemen bir VIP Kart (Token) basıp verelim
        String token = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), java.util.Collections.emptyList()));

        return ResponseEntity.ok(token); // Adamın ekranına uzun şifreli metin basılacak
    }

    // 2. GİRİŞ YAP (LOGIN)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        // Adamın şifresini doğrula (Yanlışsa burada Exception fırlar ve 403 Forbidden döner)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Şifre doğruysa adamı veritabanından bul ve kartını (Token) bas
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), java.util.Collections.emptyList()));

        return ResponseEntity.ok(token); // Yeni VIP Kartını adama teslim et
    }

    // Dışarıdan gelen İstek Paketi (DTO mantığı)
    @Data
    static class AuthRequest {
        private String username;
        private String password;
    }
}
