package com.api.ecommerce.web.dto.accessorydto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AccesoryUpdatedDTO(
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
        String brand
) {
}
