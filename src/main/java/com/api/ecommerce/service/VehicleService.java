package com.api.ecommerce.service;

import com.api.ecommerce.persistence.entity.vehicle.VehicleEntity;
import com.api.ecommerce.persistence.repository.VehicleRepository;
import com.api.ecommerce.web.dto.vehicledto.VehicleCreateDTO;
import com.api.ecommerce.web.dto.vehicledto.VehicleResponseDTO;
import com.api.ecommerce.web.dto.vehicledto.VehicleUpdatedDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    // Inyeccion del repositorio de la entidad vehiculo
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }


    // Para crear un nuevo vehiculo
    public Long createVehicle(VehicleCreateDTO vehicleCreateDTO) {
        VehicleEntity newVehicleEntity = new VehicleEntity(vehicleCreateDTO);
        return vehicleRepository.save(newVehicleEntity).getId();
    }


    // Para obtener todos los vehiculos activos
    public List<VehicleResponseDTO> findAllIsActiveTrue() {
        return vehicleRepository.findByIsActiveTrue()
                .stream()
                .map(VehicleResponseDTO::convertToVehicleResponseDTO)
                .toList();
    }


    // Para obtener todos los vehiculos incluidos los inactivos
    public List<VehicleResponseDTO> findAllIncludingInactive() {
        return vehicleRepository.findAll()
                .stream()
                .map(VehicleResponseDTO::convertToVehicleResponseDTO)
                .toList();
    }


    // Para buscar un vehiculo activo por medio de su id
    public VehicleResponseDTO findByIdAndIsActiveTrue(Long id) {
        if (vehicleRepository.existsByIdAndIsActiveTrue(id)) {
            Optional<VehicleEntity> optionalVehicle = vehicleRepository.findByIdAndIsActiveTrue(id);
            return VehicleResponseDTO.convertToVehicleResponseDTO(optionalVehicle.get());
        }
        return null;
    }


    // Para buscar un vehiculo que este activo o inactivo por medio de su id
    public VehicleResponseDTO findByIdIncludingInactive(Long id) {
        if (vehicleRepository.existsById(id)) {
            Optional<VehicleEntity> optionalVehicle = vehicleRepository.findById(id);
            return VehicleResponseDTO.convertToVehicleResponseDTO(optionalVehicle.get());
        }
        return null;
    }


    // Para buscar un vehiculo por palabras clave
    public List<VehicleResponseDTO> search(String brand, String model, Integer year, Double price,
                                           Integer doors, String color, String location
    ) {
        return vehicleRepository.search(brand, model, year, price, doors, color, location)
                .stream()
                .map(VehicleResponseDTO::convertToVehicleResponseDTO)
                .toList();
    }


    // Para actualizar un vehiculo
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


    // Para realizar eliminacion logica de un vehiculo
    @Transactional
    public void softDeleteVehicleById(Long id) {
        vehicleRepository.findByIdAndIsActiveTrue(id).ifPresent(vehicleEntity -> vehicleEntity.setIsActive(false));
    }


    // Para reactivar un vehiculo eliminado logicamente
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
