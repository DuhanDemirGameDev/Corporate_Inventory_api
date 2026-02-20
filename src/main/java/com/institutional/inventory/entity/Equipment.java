package com.institutional.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Cihazın adı (Örn: Dell Latitude, Osiloskop)

    @Column(unique = true, nullable = false)
    private String serialNumber; // Seri numarası (Benzersiz olmalı)

    private String category; // Kategori (Laptop, Monitör vb.)

    private String status; // Durum (AVAILABLE, ASSIGNED, IN_REPAIR)

    @ManyToOne // Bir personelin BİRDEN FAZLA ekipmanı olabilir, ama bir ekipman sadece BİR personele ait olabilir.
    @JoinColumn(name = "employee_id") // Veritabanında bu bağlantıyı tutacak kolonun adı
    private Employee employee;
}