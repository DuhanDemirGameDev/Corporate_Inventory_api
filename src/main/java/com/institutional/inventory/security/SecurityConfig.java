package com.institutional.inventory.security;

import com.institutional.inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    // 1. BİNAMIZIN GÜVENLİK KURALLARI (Security Filter Chain)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Token kullandığımız için CSRF korumasına gerek yok
                .authorizeHttpRequests(auth -> auth
                        // Kimlik doğrulama, kayıt olma ve Swagger URL'leri HERKESE AÇIK
                        .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Geri kalan TÜM URL'ler İÇİN VIP KART (TOKEN) ZORUNLU!
                        .anyRequest().authenticated()
                )
                // Sistemimizde "Session" (Oturum) tutmuyoruz, tamamen STATELESS (Durumsuz) JWT mimarisi kullanıyoruz.
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                // Kendi yazdığımız Gümrük Polisini (JwtFilter), standart şifre polisinden ÖNCE kapıya dik!
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 2. KİMLİK DOĞRULAYICI (Veritabanından adam bulma ve şifre kıyaslama motoru)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder()); // Şifreleri BCrypt ile çözecek
        return authProvider;
    }

    // 3. ŞİFRE KIRICI/ŞİFRELEYİCİ ALGORİTMA (BCrypt - Sektör Standardı)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 4. KİMLİK YÖNETİCİSİ
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
