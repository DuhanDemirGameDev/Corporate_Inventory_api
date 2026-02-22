package com.institutional.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // Getter, Setter işlemleri otomatik
public class EmployeeDto {
    private Long id;
    @NotBlank(message = "Personel adı kesinlikle boş bırakılamaz!")
    private String firstName;
    @NotBlank(message = "Personel soyadı kesinlikle boş bırakılamaz!")
    private String lastName;
    private String registrationNumber;
    private String department;
}
