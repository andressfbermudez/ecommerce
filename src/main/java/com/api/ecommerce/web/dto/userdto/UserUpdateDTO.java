package com.api.ecommerce.web.dto.userdto;

import jakarta.validation.constraints.Email;
import com.api.ecommerce.persistence.entity.user.Role;

public record UserUpdateDTO(

        // Si alguno de estos atributos se debe actualizar,
        // se deben actualizar todos los atributos de este bloque en conjunto.
        String username,

        @Email
        String email,

        String password,
        Role role,

        // Estos son opcionales.
        Boolean locked,
        Boolean disabled
) {
}
