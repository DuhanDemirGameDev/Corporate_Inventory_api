package com.institutional.inventory.service;

import com.institutional.inventory.dto.EmployeeDto;
import com.institutional.inventory.entity.Employee;
import com.institutional.inventory.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // SİSTEME YENİ PERSONEL EKLEME
    public EmployeeDto addEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setRegistrationNumber(employeeDto.getRegistrationNumber());
        employee.setDepartment(employeeDto.getDepartment());

        Employee savedEmployee = employeeRepository.save(employee);

        employeeDto.setId(savedEmployee.getId());
        return employeeDto;
    }

    // TÜM PERSONELLERİ LİSTELEME
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(emp -> {
            EmployeeDto dto = new EmployeeDto();
            dto.setId(emp.getId());
            dto.setFirstName(emp.getFirstName());
            dto.setLastName(emp.getLastName());
            dto.setRegistrationNumber(emp.getRegistrationNumber());
            dto.setDepartment(emp.getDepartment());
            return dto;
        }).collect(Collectors.toList());
    }
}