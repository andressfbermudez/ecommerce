package com.api.ecommerce.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import com.api.ecommerce.web.dto.userdto.LoginDTO;
import com.api.ecommerce.web.dto.userdto.UserRegisterDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok().build();
    }
}
