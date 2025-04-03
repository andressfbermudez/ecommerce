package com.api.ecommerce.web.dto;

import jakarta.validation.constraints.*;
import com.api.ecommerce.persistence.entity.vehicle.FuelType;
import com.api.ecommerce.persistence.entity.vehicle.Transmission;

public record VehicleCreateDTO(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "The name cannot have more than 255 characters")
        String name,

        @Size(max = 255, message = "The description cannot have more than 255 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "The price must be greater than 0")
        Double price,

        @NotNull(message = "Stock is mandatory")
        @Min(value = 0, message = "The stock cannot be negative")
        Integer stock,

        @NotBlank(message = "The brand is mandatory")
        @Size(max = 100, message = "The brand cannot have more than 100 characters")
        String brand,

        @NotBlank(message = "The model is mandatory")
        @Size(max = 100, message = "The model cannot have more than 100 characters")
        String model,

        @NotNull(message = "The year is mandatory")
        @Min(value = 2010, message = "The year must be valid")
        Integer year,

        @NotNull(message = "Mileage is mandatory")
        @Min(value = 0, message = "The mileage cannot be negative")
        Integer mileage,

        @NotNull(message = "Cylinder capacity is mandatory")
        @Min(value = 0, message = "The displacement cannot be negative")
        Integer engineCapacity,

        @NotNull(message = "The fuel type is mandatory. GASOLINE - DIESEL - ELECTRIC - HIBRID")
        FuelType fuelType,

        @NotNull(message = "Transmission is mandatory. MANUAL - AUTOMATIC")
        Transmission transmission,

        @NotNull(message = "The number of doors is mandatory")
        @Min(value = 2, message = "Must have at least 2 doors")
        @Max(value = 5, message = "It cannot have more than 5 doors")
        Integer doors,

        @NotBlank(message = "Color is mandatory")
        @Size(max = 100, message = "The color cannot have more than 100 characters")
        String color,

        @NotBlank(message = "Location is required")
        @Size(max = 100, message = "The location cannot have more than 100 characters")
        String location
) {
}
