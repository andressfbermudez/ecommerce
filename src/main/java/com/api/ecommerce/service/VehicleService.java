package com.api.ecommerce.service;

import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.api.ecommerce.web.dto.VehicleCreateDTO;
import com.api.ecommerce.web.dto.VehicleUpdatedDTO;
import com.api.ecommerce.web.dto.VehicleResponseDTO;
import com.api.ecommerce.persistence.entity.vehicle.Vehicle;
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
        Vehicle newVehicle = new Vehicle(vehicleCreateDTO);
        return vehicleRepository.save(newVehicle).getId();
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
            Optional<Vehicle> optionalVehicle = vehicleRepository.findByIdAndIsActiveTrue(id);
            return VehicleResponseDTO.convertToVehicleResponseDTO(optionalVehicle.get());
        }
        return null;
    }

    public VehicleResponseDTO findById(Long id) {
        if (vehicleRepository.existsById(id)) {
            Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
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
        return vehicleRepository.findByIdAndIsActiveTrue(id).map(vehicle -> {
            if (v.name() != null) vehicle.setName(v.name().trim());
            if (v.description() != null) vehicle.setDescription(v.description().trim());
            if (v.price() != null) vehicle.setPrice(v.price());
            if (v.stock() != null) vehicle.setStock(v.stock());
            if (v.brand() != null) vehicle.setBrand(v.brand().trim());
            if (v.model() != null) vehicle.setModel(v.model().trim());
            if (v.year() != null) vehicle.setYear(v.year());
            if (v.mileage() != null) vehicle.setMileage(v.mileage());
            if (v.engineCapacity() != null) vehicle.setEngineCapacity(v.engineCapacity());
            if (v.transmission() != null) vehicle.setTransmission(v.transmission());
            if (v.doors() != null) vehicle.setDoors(v.doors());
            if (v.color() != null) vehicle.setColor(v.color().trim());
            if (v.location() != null) vehicle.setLocation(v.location().trim());

            return vehicle.getId();
        }).orElse(null);
    }

    @Transactional
    public void softDeleteVehicleById(Long id) {
        vehicleRepository.findByIdAndIsActiveTrue(id).ifPresent(vehicle -> vehicle.setIsActive(false));
    }

    @Transactional
    public Long restoreVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    vehicle.setIsActive(true);
                    return vehicle.getId();
                })
                .orElse(null);
    }
// *********************************************************************************************************************
    public boolean existsById(Long id) {
        return vehicleRepository.existsById(id);
    }
}
