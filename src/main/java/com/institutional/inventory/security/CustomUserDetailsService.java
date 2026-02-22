package com.institutional.inventory.security;

import com.institutional.inventory.entity.User;
import com.institutional.inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Spring Security, giriş yapılmaya çalışıldığında bu metodu otomatik çağırır!
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Kendi veritabanımızdan adamı buluyoruz
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı sistemde yok: " + username));

        // 2. Bizim 'User' nesnemizi, Spring Security'nin anladığı 'UserDetails' nesnesine çeviriyoruz
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())) // Yetkisini (Admin/User) veriyoruz
        );
    }
}