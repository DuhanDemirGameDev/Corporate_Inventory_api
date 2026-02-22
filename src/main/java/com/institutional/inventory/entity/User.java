package com.institutional.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Giriş yaparken kullanılacak (örn: admin_ahmet)

    @Column(nullable = false)
    private String password; // Şifrelenmiş (hashlenmiş) olarak tutacağız, asla açık tutulmaz!

    @Column(nullable = false)
    private String role; // "ROLE_USER" veya "ROLE_ADMIN" yetkilerini tutacak
}