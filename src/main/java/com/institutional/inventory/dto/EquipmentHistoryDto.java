package com.institutional.inventory.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EquipmentHistoryDto {
    private Long id;
    private Long equipmentId;
    private Long employeeId;
    private String employeeName; // Arayüzde (Front-end) ismini göstermek kolay olsun diye
    private String action;
    private LocalDateTime actionDate;
    private String remarks;
}
