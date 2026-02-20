package com.institutional.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data // Getter ve Setter'ları otomatik oluşturur
@NoArgsConstructor // Parametresiz yapıcı metod (JPA için zorunlu)
@AllArgsConstructor // Tüm parametreleri içeren yapıcı metod
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String registrationNumber; // Kurumsal Sicil Numarası

    private String department; // Departman
}