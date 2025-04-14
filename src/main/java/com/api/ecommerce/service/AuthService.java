package com.api.ecommerce.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import jakarta.transaction.Transactional;
import com.api.ecommerce.web.service.JwtUtil;
import org.springframework.stereotype.Service;
import com.api.ecommerce.web.dto.userdto.LoginDTO;
import jakarta.persistence.EntityNotFoundException;
import com.api.ecommerce.web.dto.userdto.UserUpdateDTO;
import org.springframework.security.core.Authentication;
import com.api.ecommerce.web.dto.userdto.UserResponseDTO;
import com.api.ecommerce.web.dto.userdto.UserRegisterDTO;
import com.api.ecommerce.persistence.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.api.ecommerce.persistence.repository.UserRepository;
import com.api.ecommerce.web.controller.exception.ValidationException;
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

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getId(), user.getUsername(), user.getEmail(), "[PROTECTED]",
                        user.getRole(), user.getLocked(), user.getDisabled()))
                .toList();
    }

    public UserResponseDTO findUserByUsernameOrEmail(String usernameOrEmail) {
        Optional<UserEntity> optionalUserEntity =  userRepository.findByUsernameOrEmail(usernameOrEmail);
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            return new UserResponseDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(),
                    "[PASSWORD]", userEntity.getRole(), userEntity.getLocked(), userEntity.getDisabled());
        }
        return null;
    }

    @Transactional
    public void updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        validateRequiredFieldsBlock(userUpdateDTO);

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (userUpdateDTO.username() != null) userEntity.setUsername(userUpdateDTO.username());
        if (userUpdateDTO.email() != null) userEntity.setEmail(userUpdateDTO.email());
        if (userUpdateDTO.password() != null) userEntity.setPassword(encoder.encode(userUpdateDTO.password()));
        if (userUpdateDTO.role() != null) userEntity.setRole(userUpdateDTO.role());
        if (userUpdateDTO.locked() != null) userEntity.setLocked(userUpdateDTO.locked());
        if (userUpdateDTO.disabled() != null) userEntity.setDisabled(userUpdateDTO.disabled());
    }

    // Metodo para validar el caso en el que se requiere actualizar un campo relacionado al rol del usuario,
    // se debe actualizar todo el bloque de campos relacionados al rol del usuario.
    private void validateRequiredFieldsBlock(UserUpdateDTO userUpdateDTO) {
        boolean anyFieldIsNotNull = Stream.of(
                userUpdateDTO.username(),
                userUpdateDTO.email(),
                userUpdateDTO.role()
        ).anyMatch(Objects::nonNull);

        if (anyFieldIsNotNull) {
            boolean anyFieldIsNull = Stream.of(
                    userUpdateDTO.username(),
                    userUpdateDTO.email(),
                    userUpdateDTO.role()
            ).anyMatch(Objects::isNull);

            if (anyFieldIsNull) {
                throw new ValidationException("If one of the fields related to the user's role" +
                        " (username, email, password, role) is to be updated, all of them must " +
                        "be updated.");
            }
        }
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
