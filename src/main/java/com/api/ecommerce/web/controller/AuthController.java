package com.api.ecommerce.web.controller;

import jakarta.validation.Valid;
import com.api.ecommerce.service.AuthService;
import org.springframework.http.ResponseEntity;
import com.api.ecommerce.web.dto.userdto.LoginDTO;
import com.api.ecommerce.web.dto.userdto.UserRegisterDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        authService.createNewUser(userRegisterDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        authService.login(loginDTO);
        return ResponseEntity.ok().build();
    }
}
