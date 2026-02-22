package com.institutional.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // Getter ve Setter'ları yine otomatik hallediyoruz
public class EquipmentDto {
    private Long id;
    @NotBlank(message = "Ekipman adı kesinlikle boş bırakılamaz!")
    private String name;

    @NotBlank(message = "Cihaza ait bir seri numarası girmek zorundasınız!")
    private String serialNumber;

    @NotBlank(message = "Kategori alanı boş olamaz!")
    private String category;
    private String status;
    private Long employeeId;
}