package com.institutional.inventory.service;

import com.institutional.inventory.dto.EquipmentDto;
import com.institutional.inventory.dto.EquipmentHistoryDto;
import com.institutional.inventory.entity.Employee;
import com.institutional.inventory.entity.Equipment;
import com.institutional.inventory.entity.EquipmentHistory;
import com.institutional.inventory.exception.ResourceNotFoundException;
import com.institutional.inventory.repository.EmployeeRepository;
import com.institutional.inventory.repository.EquipmentHistoryRepository;
import com.institutional.inventory.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EmployeeRepository employeeRepository;
    private final EquipmentHistoryRepository historyRepository; // Bunu unutmamak gerek!

    // 1. CİHAZ EKLEME
    public EquipmentDto addEquipment(EquipmentDto equipmentDto) {
        Equipment equipment = new Equipment();
        equipment.setName(equipmentDto.getName());
        equipment.setSerialNumber(equipmentDto.getSerialNumber());
        equipment.setCategory(equipmentDto.getCategory());
        equipment.setStatus("AVAILABLE");

        Equipment savedEquipment = equipmentRepository.save(equipment);

        equipmentDto.setId(savedEquipment.getId());
        equipmentDto.setStatus(savedEquipment.getStatus());
        return equipmentDto;
    }

    // 2. TÜM CİHAZLARI GETİRME
    public List<EquipmentDto> getAllEquipments() {
        return equipmentRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // 3. ZİMMETLEME (KARA KUTU KAYDI EKLENDİ)
    public EquipmentDto assignEquipment(Long equipmentId, Long employeeId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Belirtilen ID ile cihaz bulunamadı: " + equipmentId));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Belirtilen ID ile personel bulunamadı: " + employeeId));

        if (!"AVAILABLE".equals(equipment.getStatus())) {
            throw new RuntimeException("Bu cihaz şu an boşta değil! Durumu: " + equipment.getStatus());
        }

        // Zimmetle
        equipment.setEmployee(employee);
        equipment.setStatus("ASSIGNED");
        Equipment savedEquipment = equipmentRepository.save(equipment);

        // --- TARİHÇE KAYDI (AUDIT) ---
        EquipmentHistory history = new EquipmentHistory();
        history.setEquipment(savedEquipment);
        history.setEmployee(employee);
        history.setAction("ASSIGNED");
        history.setActionDate(LocalDateTime.now());
        history.setRemarks("Cihaz personele zimmetlendi.");
        historyRepository.save(history);
        // -----------------------------

        return mapToDto(savedEquipment);
    }

    // 4. GEÇMİŞİ GETİR (DÜZELTİLDİ: ARTIK CİHAZ YOKSA HATA VERİYOR!)
    public List<EquipmentHistoryDto> getEquipmentHistory(Long equipmentId) {
        // Önce cihaz var mı diye kontrol et! Yoksa hata fırlat.
        if (!equipmentRepository.existsById(equipmentId)) {
            throw new ResourceNotFoundException("Geçmişi sorgulanan cihaz bulunamadı: " + equipmentId);
        }

        List<EquipmentHistory> historyList = historyRepository.findByEquipmentIdOrderByActionDateDesc(equipmentId);

        return historyList.stream().map(hist -> {
            EquipmentHistoryDto dto = new EquipmentHistoryDto();
            dto.setId(hist.getId());
            dto.setEquipmentId(hist.getEquipment().getId());
            dto.setAction(hist.getAction());
            dto.setActionDate(hist.getActionDate());
            dto.setRemarks(hist.getRemarks());

            if (hist.getEmployee() != null) {
                dto.setEmployeeId(hist.getEmployee().getId());
                dto.setEmployeeName(hist.getEmployee().getFirstName() + " " + hist.getEmployee().getLastName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // YARDIMCI METOD (Kod tekrarını önlemek için)
    private EquipmentDto mapToDto(Equipment equipment) {
        EquipmentDto dto = new EquipmentDto();
        dto.setId(equipment.getId());
        dto.setName(equipment.getName());
        dto.setSerialNumber(equipment.getSerialNumber());
        dto.setCategory(equipment.getCategory());
        dto.setStatus(equipment.getStatus());
        if (equipment.getEmployee() != null) {
            dto.setEmployeeId(equipment.getEmployee().getId());
        }
        return dto;
    }

    // Custom Queries (Daha önce eklediğimiz filtreleme metodları)
    public List<EquipmentDto> getEquipmentsByStatus(String status) {
        return equipmentRepository.findByStatus(status).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<EquipmentDto> getEquipmentsByEmployeeId(Long employeeId) {
        return equipmentRepository.findByEmployeeId(employeeId).stream().map(this::mapToDto).collect(Collectors.toList());
    }
}