package com.api.ecommerce.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.api.ecommerce.web.dto.VehicleCreateDTO;
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

    public List<VehicleResponseDTO> findAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(VehicleResponseDTO::convertToVehicleResponseDAO)
                .toList();
    }

    public VehicleResponseDTO findById(Long id) {
        if (vehicleRepository.existsById(id)) {
            Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
            return VehicleResponseDTO.convertToVehicleResponseDAO(optionalVehicle.get());
        }
        return null;
    }
}
