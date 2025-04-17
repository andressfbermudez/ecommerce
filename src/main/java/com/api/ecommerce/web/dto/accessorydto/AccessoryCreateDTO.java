package com.api.ecommerce.web.dto.accessorydto;

import jakarta.validation.constraints.*;

public record AccessoryCreateDTO(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "The name cannot have more than 100 characters")
        String name,

        @Size(max = 255, message = "The description cannot have more than 255 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "The price must be greater than 0")
        Double price,

        @NotNull(message = "Stock is mandatory")
        @Min(value = 0, message = "The stock cannot be negative")
        Integer Stock,

        @NotBlank(message = "The brand is mandatory")
        @Size(max = 100, message = "The brand cannot have more than 100 characters")
        String brand
) {
}
