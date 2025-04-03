package com.api.ecommerce.web.dto;

import jakarta.validation.constraints.*;
import com.api.ecommerce.persistence.entity.vehicle.FuelType;
import com.api.ecommerce.persistence.entity.vehicle.Transmission;

public record VehicleUpdatedDTO(

        @Size(max = 100, message = "The name cannot have more than 100 characters")
        @Pattern(regexp = "^(?!\\s*$).+", message = "The name cannot be empty or blank")
        String name,

        @Size(max = 255, message = "The description cannot have more than 255 characters")
        @Pattern(regexp = "^(?!\\s*$).+", message = "The description cannot be empty or blank")
        String description,

        @DecimalMin(value = "0.0", inclusive = false, message = "The price must be greater than 0")
        Double price,

        @Min(value = 0, message = "The stock cannot be negative")
        Integer stock,

        @Size(max = 100, message = "The brand cannot have more than 100 characters")
        @Pattern(regexp = "^(?!\\s*$).+", message = "The brand cannot be empty or blank")
        String brand,

        @Size(max = 100, message = "The model cannot have more than 100 characters")
        @Pattern(regexp = "^(?!\\s*$).+", message = "The model cannot be empty or blank")
        String model,

        @Min(value = 2010, message = "The year must be 2010 or later")
        Integer year,

        @Min(value = 0, message = "The mileage cannot be negative")
        Integer mileage,

        @Min(value = 0, message = "The engine capacity cannot be negative")
        Integer engineCapacity,

        FuelType fuelType,

        Transmission transmission,

        @Min(value = 2, message = "Must have at least 2 doors")
        @Max(value = 5, message = "It cannot have more than 5 doors")
        Integer doors,

        @Size(max = 100, message = "The color cannot have more than 100 characters")
        @Pattern(regexp = "^(?!\\s*$).+", message = "The color cannot be empty or blank")
        String color,

        @Size(max = 100, message = "The location cannot have more than 100 characters")
        @Pattern(regexp = "^(?!\\s*$).+", message = "The location cannot be empty or blank")
        String location
) {
}
