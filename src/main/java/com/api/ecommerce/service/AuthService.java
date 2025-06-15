package com.api.ecommerce.service;

import com.api.ecommerce.persistence.entity.user.UserEntity;
import com.api.ecommerce.persistence.repository.UserRepository;
import com.api.ecommerce.service.email.EmailService;
import com.api.ecommerce.web.controller.exception.ValidationException;
import com.api.ecommerce.web.dto.userdto.LoginDTO;
import com.api.ecommerce.web.dto.userdto.UserRegisterDTO;
import com.api.ecommerce.web.dto.userdto.UserResponseDTO;
import com.api.ecommerce.web.dto.userdto.UserUpdateDTO;
import com.api.ecommerce.web.service.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AuthService {

    // Para inyectar los servicios necesarios
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Autowired
    public AuthService(UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil, EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    // Este metodo realiza el proceso de autenticacion y si es exitoso retorna un JWT (String).
    public Boolean login(LoginDTO loginDTO) throws MessagingException, IOException {
        UsernamePasswordAuthenticationToken user =
                new UsernamePasswordAuthenticationToken(loginDTO.usernameOrEmail(), loginDTO.password());

        // Disparar el proceso de autenticacion con el usuario que fue enviado.
        Authentication authentication = this.authenticationManager.authenticate(user);

        // Si el usuario se autentico correctamente se retorna un JWT (String).
        if (authentication.isAuthenticated()) {
            String jwt = jwtUtil.create(loginDTO.usernameOrEmail());

            // Obtener el email del usuario autenticado para enviarle alli el JWT.
            String emailAuthenticateUser =
                    userRepository.findByUsernameOrEmail(jwtUtil.getSubject(jwt)).get().getEmail();

            System.out.println("\nEmail: " + emailAuthenticateUser);

            // Enviar el JWT al correo del usuario autenticado.
            emailService.sendJWTByEmail(emailAuthenticateUser, jwt);
            return true;
        }
        return false;
    }


    // Para crear un nuevo usuario
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


    // Para obtener todos los usuarios
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getId(), user.getUsername(), user.getEmail(), "[PROTECTED]",
                        user.getRole(), user.getLocked(), user.getDisabled()))
                .toList();
    }


    // Para obtener un usuario por medio de su nombre o su email
    public UserResponseDTO findUserByUsernameOrEmail(String usernameOrEmail) {
        Optional<UserEntity> optionalUserEntity =  userRepository.findByUsernameOrEmail(usernameOrEmail);
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            return new UserResponseDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(),
                    "[PASSWORD]", userEntity.getRole(), userEntity.getLocked(), userEntity.getDisabled());
        }
        return null;
    }


    // Para actualizar un usuario
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

    // Para verificar si existe un usuario
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
