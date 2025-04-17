package com.api.ecommerce.web.controller;

import com.api.ecommerce.service.AuthService;
import com.api.ecommerce.web.dto.userdto.LoginDTO;
import com.api.ecommerce.web.dto.userdto.UserRegisterDTO;
import com.api.ecommerce.web.dto.userdto.UserResponseDTO;
import com.api.ecommerce.web.dto.userdto.UserUpdateDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        authService.createNewUser(userRegisterDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Void> loginUser(@Valid @RequestBody LoginDTO loginDTO) throws MessagingException, IOException {
        if (authService.login(loginDTO)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> userResponseDTOS = authService.getAllUsers();
        return ResponseEntity.ok(userResponseDTOS);
    }

    @GetMapping("/search/{usernameOrEmail}")
    public ResponseEntity<UserResponseDTO> getUsers(@Valid @PathVariable String usernameOrEmail) {
        UserResponseDTO userResponseDTO = authService.findUserByUsernameOrEmail(usernameOrEmail);
        if (userResponseDTO != null) {
            return ResponseEntity.ok(userResponseDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@Valid @PathVariable Long id,
                                           @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        if (authService.existsById(id)) {
            authService.updateUser(id, userUpdateDTO);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
