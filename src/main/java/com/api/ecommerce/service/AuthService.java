package com.api.ecommerce.service;

import com.api.ecommerce.web.service.JwtUtil;
import org.springframework.stereotype.Service;
import com.api.ecommerce.web.dto.userdto.LoginDTO;
import org.springframework.security.core.Authentication;
import com.api.ecommerce.web.dto.userdto.UserRegisterDTO;
import com.api.ecommerce.persistence.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.api.ecommerce.persistence.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // Este metodo realiza el proceso de autenticacion y si es exitoso retorna un JWT (String).
    public String login(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken user =
                new UsernamePasswordAuthenticationToken(loginDTO.usernameOrEmail(), loginDTO.password());

        // Disparar el proceso de autenticacion con el usuario que fue enviado.
        Authentication authentication = this.authenticationManager.authenticate(user);

        // Si el usuario se autentico correctamente se retorna un JWT (String).
        if (authentication.isAuthenticated()) {
            // Retornar un JWT con el usuario autenticado.
            return jwtUtil.create(loginDTO.usernameOrEmail());
        }
        return null;
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
}
