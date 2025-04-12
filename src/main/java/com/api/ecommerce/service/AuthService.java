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

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createNewUser(UserRegisterDTO userRegisterDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userRegisterDTO.password());

        UserEntity newUser = new UserEntity(
                null, userRegisterDTO.username(), userRegisterDTO.email(),
                hashedPassword, userRegisterDTO.role()
        );
        userRepository.save(newUser);
    }

    public void login(LoginDTO loginDTO) {
    }
}
