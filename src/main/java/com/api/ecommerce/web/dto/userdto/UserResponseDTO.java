package com.api.ecommerce.web.dto.userdto;

import com.api.ecommerce.persistence.entity.user.Role;

public record UserResponseDTO(

        Long id,
        String username,
        String email,
        String password,
        Role role,
        Boolean locked,
        Boolean disabled
) {
    public UserResponseDTO(Long id,
                           String username,
                           String email,
                           String password,
                           Role role,
                           Boolean locked,
                           Boolean disabled
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.locked = locked;
        this.disabled = disabled;
    }
}
