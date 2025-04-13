package com.api.ecommerce.web.config;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Desactivar CSRF (recomendado si se usa JWT o solo APIs)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Definir political de sesión (stateless para APIs)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Habilitar Basic Authentication
                .httpBasic(Customizer.withDefaults())

                // 3. Configurar autorización de requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/api/users/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/vehicles/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/vehicles/all").hasAnyRole("ADMIN", "EMPLOYEE", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/api/vehicles/all-including-inactive").hasRole("ADMIN")
                        .requestMatchers(new RegexRequestMatcher("^/api/vehicles/\\d+$", "GET")).hasAnyRole("ADMIN", "EMPLOYEE", "CUSTOMER")
                        .requestMatchers(new RegexRequestMatcher("^/api/vehicles/find/\\d+$", "GET")).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/vehicles/search").hasAnyRole("ADMIN", "EMPLOYEE", "CUSTOMER")
                        .requestMatchers(new RegexRequestMatcher("^/api/vehicles/\\d+$", "PUT")).hasRole("ADMIN")
                        .requestMatchers(new RegexRequestMatcher("^/api/vehicles/\\d+$", "DELETE")).hasRole("ADMIN")
                        .requestMatchers(new RegexRequestMatcher("^/api/vehicles/restore/\\d+$", "PUT")).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .build();
    }

//    @Bean
//    public UserDetailsService inMemoryUsers() {
//        /*
//         * Crear usuarios propios en memoria:
//         *
//         * Spring reconocera que vamos a usar un nuevo usuario creado en memoria y dejara de
//         * generar un usuario y una contrasena por defecto
//        */
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails employee = User.builder()
//                .username("employee")
//                .password(passwordEncoder().encode("employee"))
//                .roles("EMPLOYEE")
//                .build();
//
//        UserDetails customer = User.builder()
//                .username("customer")
//                .password(passwordEncoder().encode("customer"))
//                .roles("CUSTOMER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, employee, customer);
//    }

    // Este metodo es para obtener un AuthenticationManager y poder inyectarlo en un servicio.
    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Para encriptar las contrasenas con Bcrypt Algorithm.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
