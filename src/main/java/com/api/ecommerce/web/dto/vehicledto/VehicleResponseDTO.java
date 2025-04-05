package com.api.ecommerce.web.dto.vehicledto;

import java.time.LocalDateTime;
import com.api.ecommerce.persistence.entity.vehicle.VehicleEntity;
import com.api.ecommerce.persistence.entity.vehicle.FuelType;
import com.api.ecommerce.persistence.entity.vehicle.Transmission;

public record VehicleResponseDTO(
        Long id,
        String name,
        String description,
        Double price,
        Integer stock,
        String brand,
        String model,
        Integer year,
        Integer mileage,
        Integer engineCapacity,
        FuelType fuelType,
        Transmission transmission,
        Integer doors,
        String color,
        String location,
        String category,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
    public static VehicleResponseDTO convertToVehicleResponseDTO(VehicleEntity vehicleEntity) {
        return new VehicleResponseDTO(
                vehicleEntity.getId(), vehicleEntity.getName(), vehicleEntity.getDescription(), vehicleEntity.getPrice(),
                vehicleEntity.getStock(), vehicleEntity.getBrand(), vehicleEntity.getModel(), vehicleEntity.getYear(),
                vehicleEntity.getMileage(), vehicleEntity.getEngineCapacity(), vehicleEntity.getFuelType(),
                vehicleEntity.getTransmission(), vehicleEntity.getDoors(), vehicleEntity.getColor(),
                vehicleEntity.getLocation(), vehicleEntity.getCategory().getName(), vehicleEntity.getCreatedDate(),
                vehicleEntity.getLastModifiedDate()
        );
    }
}
