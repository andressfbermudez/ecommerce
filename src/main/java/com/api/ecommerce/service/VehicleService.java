package com.api.ecommerce.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.api.ecommerce.web.dao.VehicleCreateDAO;
import com.api.ecommerce.web.dao.VehicleResponseDAO;
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

    public void createVehicle(VehicleCreateDAO vehicleCreateDAO) {
        Vehicle newVehicle = new Vehicle(vehicleCreateDAO);
        vehicleRepository.save(newVehicle);
    }

    public List<VehicleResponseDAO> findAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(VehicleResponseDAO::convertToVehicle)
                .toList();
    }
}
