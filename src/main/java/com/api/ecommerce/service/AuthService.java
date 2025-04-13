package com.api.ecommerce.service;

import org.springframework.stereotype.Service;
import com.api.ecommerce.web.dto.userdto.LoginDTO;
import com.api.ecommerce.web.dto.userdto.UserRegisterDTO;
import com.api.ecommerce.persistence.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.api.ecommerce.persistence.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createNewUser(UserRegisterDTO userRegisterDTO) {
        // Se recibe de userRegisterDTO todos los datos ingresados por el usuario,
        // entre esos datos llega la contrasena sin encriptar, antes de almacenarla
        // creamos un nuevo DTO con la contrasena encriptada.
        UserRegisterDTO userRegisterDTPasswordEncrypted = new UserRegisterDTO(
                userRegisterDTO.username(), userRegisterDTO.email(),
                encoder.encode(userRegisterDTO.password()), userRegisterDTO.role()
        );
        UserEntity newUser = new UserEntity(userRegisterDTPasswordEncrypted);
        userRepository.save(newUser);
    }

    public void login(LoginDTO loginDTO) {
    }
}
