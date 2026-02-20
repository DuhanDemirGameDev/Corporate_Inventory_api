package com.institutional.inventory.repository;

import com.institutional.inventory.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Bu sınıfın veritabanı işlemlerinden sorumlu olduğunu Spring'e söyler
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Tebrikler! Şu an hiçbir kod yazmadan veritabanına kayıt ekleme (save),
    // silme (delete), ID ile bulma (findById) yeteneklerini kazandın.

}