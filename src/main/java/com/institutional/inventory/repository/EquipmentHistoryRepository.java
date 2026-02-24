package com.institutional.inventory.repository;

import com.institutional.inventory.entity.EquipmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentHistoryRepository extends JpaRepository<EquipmentHistory, Long> {

    // JPA Sihri: Cihaz ID'sine göre bul ve Tarihe göre Azalan (Yeniden eskiye) sırala!
    List<EquipmentHistory> findByEquipmentIdOrderByActionDateDesc(Long equipmentId);

}
