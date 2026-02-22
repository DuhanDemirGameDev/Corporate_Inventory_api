package com.institutional.inventory.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Gelen isteğin başlığında (Header) VIP Kart (Token) var mı bakıyoruz.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Kart yoksa veya "Bearer " ile başlamıyorsa, adamı hiç yormadan diğer filtreye yolla (Giremez).
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. "Bearer " kelimesinden sonrasını (asıl şifreli kartı) kesip alıyoruz.
        jwt = authHeader.substring(7);
        // 3. Kartın içindeki ismi okuyoruz (Senin sorduğun o sihirli metod).
        username = jwtService.extractUsername(jwt);

        // 4. İsim boş değilse ve adam henüz sisteme giriş yapmamışsa:
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 5. Kart geçerli mi? (Süresi dolmuş mu, isim doğru mu?)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Her şey mükemmelse, adama "Geçiş İzni" veriyoruz.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // İşi bitir ve isteği hedefe (Controller'a) gönder.
        filterChain.doFilter(request, response);
    }
}
