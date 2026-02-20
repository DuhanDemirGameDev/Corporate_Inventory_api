package com.institutional.inventory.service;

import com.institutional.inventory.dto.EquipmentDto;
import com.institutional.inventory.entity.Employee;
import com.institutional.inventory.entity.Equipment;
import com.institutional.inventory.repository.EmployeeRepository;
import com.institutional.inventory.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Spring'e buranın "İş Mantığı Merkezi" olduğunu söyler.
@RequiredArgsConstructor // (Mülakat Tüyosu) Otomatik Dependency Injection yapar.
public class EquipmentService {

    // Service katmanı, veritabanına erişmek için Repository'yi çağırır.
    private final EquipmentRepository equipmentRepository;
    private final EmployeeRepository employeeRepository; // BUNU EKLEDİK
    // 1. SİSTEME YENİ BİR EKİPMAN EKLEME METODU
    public EquipmentDto addEquipment(EquipmentDto equipmentDto) {
        // Dışarıdan DTO (Kurye) olarak gelen veriyi, veritabanına yazılacak Entity'ye çeviriyoruz.
        Equipment equipment = new Equipment();
        equipment.setName(equipmentDto.getName());
        equipment.setSerialNumber(equipmentDto.getSerialNumber());
        equipment.setCategory(equipmentDto.getCategory());

        // İş Kuralı: Yeni eklenen bir cihaz her zaman "BOŞTA" yani "AVAILABLE" durumundadır.
        equipment.setStatus("AVAILABLE");

        // Repository'nin save metodu sayesinde veritabanına kaydediyoruz
        Equipment savedEquipment = equipmentRepository.save(equipment);

        // Kaydedilen ve veritabanından otomatik ID alan veriyi tekrar DTO'ya çevirip geri döndürüyoruz
        equipmentDto.setId(savedEquipment.getId());
        equipmentDto.setStatus(savedEquipment.getStatus());
        return equipmentDto;
    }

    // 2. TÜM EKİPMANLARI LİSTELEME METODU
    public List<EquipmentDto> getAllEquipments() {
        List<Equipment> equipments = equipmentRepository.findAll();

        // Java Stream API kullanarak veritabanından gelen Entity listesini DTO listesine çeviriyoruz
        return equipments.stream().map(equip -> {
            EquipmentDto dto = new EquipmentDto();
            dto.setId(equip.getId());
            dto.setName(equip.getName());
            dto.setSerialNumber(equip.getSerialNumber());
            dto.setCategory(equip.getCategory());
            dto.setStatus(equip.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    // ZİMMETLEME (ASSIGN) İŞLEMİ
    public EquipmentDto assignEquipment(Long equipmentId, Long employeeId) {

        // 1. Ekipmanı bul, yoksa hata fırlat
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Cihaz bulunamadı!"));

        // 2. Personeli bul, yoksa hata fırlat
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Personel bulunamadı!"));

        // 3. İŞ KURALI (Business Logic): Cihaz boşta mı?
        if (!equipment.getStatus().equals("AVAILABLE")) {
            throw new RuntimeException("Bu cihaz şu an boşta değil! Durumu: " + equipment.getStatus());
        }

        // 4. Zimmetleme işlemini yap
        equipment.setEmployee(employee);
        equipment.setStatus("ASSIGNED"); // Cihaz artık birinde, durumunu güncelle

        // 5. Veritabanına kaydet
        Equipment savedEquipment = equipmentRepository.save(equipment);

        // 6. Sonucu DTO'ya çevir ve döndür
        EquipmentDto dto = new EquipmentDto();
        dto.setId(savedEquipment.getId());
        dto.setName(savedEquipment.getName());
        dto.setSerialNumber(savedEquipment.getSerialNumber());
        dto.setCategory(savedEquipment.getCategory());
        dto.setStatus(savedEquipment.getStatus());
        dto.setEmployeeId(savedEquipment.getEmployee().getId()); // Kime verildiğini DTO'ya ekle

        return dto;
    }
}