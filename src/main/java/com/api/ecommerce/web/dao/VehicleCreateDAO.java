package com.api.ecommerce.web.dao;

import com.api.ecommerce.persistence.entity.Category;
import com.api.ecommerce.persistence.entity.vehicle.FuelType;
import com.api.ecommerce.persistence.entity.vehicle.Transmission;

public record VehicleCreateDAO(
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
        Category category
) {
}
