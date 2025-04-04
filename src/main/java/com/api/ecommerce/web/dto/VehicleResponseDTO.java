package com.api.ecommerce.web.dto;

import java.time.LocalDateTime;
import com.api.ecommerce.persistence.entity.vehicle.Vehicle;
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
    public static VehicleResponseDTO convertToVehicleResponseDTO(Vehicle vehicle) {
        return new VehicleResponseDTO(
                vehicle.getId(), vehicle.getName(), vehicle.getDescription(), vehicle.getPrice(),
                vehicle.getStock(), vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(),
                vehicle.getMileage(), vehicle.getEngineCapacity(), vehicle.getFuelType(),
                vehicle.getTransmission(), vehicle.getDoors(), vehicle.getColor(),
                vehicle.getLocation(), vehicle.getCategory().getName(), vehicle.getCreatedDate(),
                vehicle.getLastModifiedDate()
        );
    }
}
