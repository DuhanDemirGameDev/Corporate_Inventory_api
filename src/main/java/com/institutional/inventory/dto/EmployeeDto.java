package com.institutional.inventory.dto;

import lombok.Data;

@Data // Getter, Setter işlemleri otomatik
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String registrationNumber;
    private String department;
}
