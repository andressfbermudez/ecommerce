package com.api.ecommerce.web.dto.userdto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(

        @Email(message = "Invalid email")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters long.")
        String password
) {
}
