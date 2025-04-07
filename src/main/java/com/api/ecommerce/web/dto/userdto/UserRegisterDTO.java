package com.api.ecommerce.web.dto.userdto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import com.api.ecommerce.persistence.entity.user.Role;

public record UserRegisterDTO(

        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters.")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "The email must be in a valid format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "The password must be at least 6 characters long.")
        String password,

        @NotNull(message = "The role is mandatory")
        Role role
) {
}
