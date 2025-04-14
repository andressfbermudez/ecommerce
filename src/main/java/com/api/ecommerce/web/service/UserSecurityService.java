package com.api.ecommerce.web.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import com.api.ecommerce.persistence.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.api.ecommerce.persistence.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        /*
         * De esta forma ya esta funcionando la autenticacion con usuarios que
         * esten almacenados en la base de datos.
        */
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsernameOrEmail(usernameOrEmail);

        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            return User.builder()
                    .username(userEntity.getUsername())
                    .password(userEntity.getPassword())
                    .roles(String.valueOf(userEntity.getRole()))
                    .accountLocked(userEntity.getLocked())
                    .disabled(userEntity.getDisabled())
                    .build();
        }
        throw new UsernameNotFoundException("User: " + usernameOrEmail + " not found");
    }
}
