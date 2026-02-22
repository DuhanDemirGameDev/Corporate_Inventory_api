package com.institutional.inventory.controller;

import com.institutional.inventory.dto.EquipmentDto;
import com.institutional.inventory.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController // Bu sınıfın bir REST API uç noktası olduğunu belirtir
@RequestMapping("/api/v1/equipments") // İnternetten bu URL ile ulaşacağız
@RequiredArgsConstructor // Service'i otomatik olarak buraya bağlar (Constructor Injection)
public class EquipmentController {

    private final EquipmentService equipmentService;

    // SİSTEME YENİ EKİPMAN EKLEME UCU (POST İSTEĞİ)
    @PostMapping
    public ResponseEntity<EquipmentDto> addEquipment(@Valid @RequestBody EquipmentDto equipmentDto) {
        // Dışarıdan (internetten) gelen JSON verisini Service'e yolluyoruz
        EquipmentDto savedEquipment = equipmentService.addEquipment(equipmentDto);

        // İşlem başarılıysa 201 (Created - Oluşturuldu) koduyla beraber veriyi geri dönüyoruz
        return new ResponseEntity<>(savedEquipment, HttpStatus.CREATED);
    }

    // SİSTEMDEKİ TÜM EKİPMANLARI LİSTELEME UCU (GET İSTEĞİ)
    @GetMapping
    public ResponseEntity<List<EquipmentDto>> getAllEquipments() {
        // Service'ten tüm cihazları istiyoruz
        List<EquipmentDto> equipments = equipmentService.getAllEquipments();

        // 200 (OK - Başarılı) koduyla listeyi geri dönüyoruz
        return new ResponseEntity<>(equipments, HttpStatus.OK);
    }

    // ZİMMETLEME UCU (PUT İSTEĞİ)
    // Örnek URL: http://localhost:8080/api/v1/equipments/1/assign/5 (1 ID'li cihazı 5 ID'li personele ver)
    @PutMapping("/{equipmentId}/assign/{employeeId}")
    public ResponseEntity<EquipmentDto> assignEquipment(@PathVariable Long equipmentId, @PathVariable Long employeeId) {
        EquipmentDto updatedEquipment = equipmentService.assignEquipment(equipmentId, employeeId);
        return new ResponseEntity<>(updatedEquipment, HttpStatus.OK);
    }

    // Örnek URL: GET http://localhost:8080/api/v1/equipments/status/AVAILABLE
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EquipmentDto>> getEquipmentsByStatus(@PathVariable String status) {
        List<EquipmentDto> equipments = equipmentService.getEquipmentsByStatus(status);
        return new ResponseEntity<>(equipments, HttpStatus.OK);
    }

    // Örnek URL: GET http://localhost:8080/api/v1/equipments/employee/1
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EquipmentDto>> getEquipmentsByEmployeeId(@PathVariable Long employeeId) {
        List<EquipmentDto> equipments = equipmentService.getEquipmentsByEmployeeId(employeeId);
        return new ResponseEntity<>(equipments, HttpStatus.OK);
    }
}