package com.institutional.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private LocalDateTime timestamp; // Hatanın olduğu zaman
    private String message;          // Bizim yazdığımız mesaj (Örn: Cihaz bulunamadı)
    private String details;          // Hatanın detayı (Örn: /api/v1/equipments/99)
}