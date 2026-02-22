package com.institutional.inventory.repository;

import com.institutional.inventory.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    // JPA'nın Sihri: Metot ismini İngilizce kurallarına göre yazarsan,
    // arkaplandaki SQL sorgusunu kendi yazar!

    // Örneğin; sadece "Boşta (AVAILABLE)" olan ekipmanları getirmesini istiyoruz:
    List<Equipment> findByStatus(String status);

    // 2. Sadece belirli bir personele zimmetli olan cihazları getir.
    // JPA arka planda JOIN atarak employee_id'ye göre filtreleme yapar.
    List<Equipment> findByEmployeeId(Long employeeId);
}