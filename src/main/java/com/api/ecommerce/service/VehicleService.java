package com.api.ecommerce.service;

import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.api.ecommerce.web.dto.vehicledto.VehicleCreateDTO;
import com.api.ecommerce.web.dto.vehicledto.VehicleUpdatedDTO;
import com.api.ecommerce.web.dto.vehicledto.VehicleResponseDTO;
import com.api.ecommerce.persistence.entity.vehicle.VehicleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.api.ecommerce.persistence.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Long createVehicle(VehicleCreateDTO vehicleCreateDTO) {
        VehicleEntity newVehicleEntity = new VehicleEntity(vehicleCreateDTO);
        return vehicleRepository.save(newVehicleEntity).getId();
    }

    public List<VehicleResponseDTO> findAllIsActiveTrue() {
        return vehicleRepository.findByIsActiveTrue()
                .stream()
                .map(VehicleResponseDTO::convertToVehicleResponseDTO)
                .toList();
    }

    public List<VehicleResponseDTO> findAllIncludingInactive() {
        return vehicleRepository.findAll()
                .stream()
                .map(VehicleResponseDTO::convertToVehicleResponseDTO)
                .toList();
    }

    public VehicleResponseDTO findByIdAndIsActiveTrue(Long id) {
        if (vehicleRepository.existsByIdAndIsActiveTrue(id)) {
            Optional<VehicleEntity> optionalVehicle = vehicleRepository.findByIdAndIsActiveTrue(id);
            return VehicleResponseDTO.convertToVehicleResponseDTO(optionalVehicle.get());
        }
        return null;
    }

    public VehicleResponseDTO findByIdIncludingInactive(Long id) {
        if (vehicleRepository.existsById(id)) {
            Optional<VehicleEntity> optionalVehicle = vehicleRepository.findById(id);
            return VehicleResponseDTO.convertToVehicleResponseDTO(optionalVehicle.get());
        }
        return null;
    }

    public List<VehicleResponseDTO> search(String brand, String model, Integer year, Double price,
                                           Integer doors, String color, String location
    ) {
        return vehicleRepository.search(brand, model, year, price, doors, color, location)
                .stream()
                .map(VehicleResponseDTO::convertToVehicleResponseDTO)
                .toList();
    }

    @Transactional
    public Long updateVehicle(Long id, VehicleUpdatedDTO v) {
        return vehicleRepository.findByIdAndIsActiveTrue(id).map(vehicleEntity -> {
            if (v.name() != null) vehicleEntity.setName(v.name().trim());
            if (v.description() != null) vehicleEntity.setDescription(v.description().trim());
            if (v.price() != null) vehicleEntity.setPrice(v.price());
            if (v.stock() != null) vehicleEntity.setStock(v.stock());
            if (v.brand() != null) vehicleEntity.setBrand(v.brand().trim());
            if (v.model() != null) vehicleEntity.setModel(v.model().trim());
            if (v.year() != null) vehicleEntity.setYear(v.year());
            if (v.mileage() != null) vehicleEntity.setMileage(v.mileage());
            if (v.engineCapacity() != null) vehicleEntity.setEngineCapacity(v.engineCapacity());
            if (v.transmission() != null) vehicleEntity.setTransmission(v.transmission());
            if (v.doors() != null) vehicleEntity.setDoors(v.doors());
            if (v.color() != null) vehicleEntity.setColor(v.color().trim());
            if (v.location() != null) vehicleEntity.setLocation(v.location().trim());

            return vehicleEntity.getId();
        }).orElse(null);
    }

    @Transactional
    public void softDeleteVehicleById(Long id) {
        vehicleRepository.findByIdAndIsActiveTrue(id).ifPresent(vehicleEntity -> vehicleEntity.setIsActive(false));
    }

    @Transactional
    public Long restoreVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicleEntity -> {
                    vehicleEntity.setIsActive(true);
                    return vehicleEntity.getId();
                })
                .orElse(null);
    }
// *********************************************************************************************************************
    public boolean existsById(Long id) {
        return vehicleRepository.existsById(id);
    }
}
