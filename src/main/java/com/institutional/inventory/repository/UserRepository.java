package com.institutional.inventory.repository;

import com.institutional.inventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA'nın gücü: Sadece ismini yazıyoruz, o gidip username ile arama yapıyor!
    // Optional yapıyoruz çünkü o isimde biri veritabanında hiç olmayabilir (Hatalı giriş).
    Optional<User> findByUsername(String username);

}
