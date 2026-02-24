package com.institutional.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hangi cihazın geçmişi?
    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    // Kime verildi? (Cihaz boşa çıkarıldığında null olabileceği için nullable=false YAZMIYORUZ)
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    // Ne yapıldı? (Örn: "ASSIGNED", "RETURNED", "REPAIRED")
    @Column(nullable = false)
    private String action;

    // Ne zaman yapıldı?
    @Column(nullable = false)
    private LocalDateTime actionDate;

    // Ekstra not (Örn: "Ekranı kırıktı, tamirden geldi personele verildi")
    private String remarks;
}
