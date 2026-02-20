package com.institutional.inventory.dto;

import lombok.Data;

@Data // Getter ve Setter'ları yine otomatik hallediyoruz
public class EquipmentDto {
    private Long id;
    private String name;
    private String serialNumber;
    private String category;
    private String status;
    private Long employeeId;
}