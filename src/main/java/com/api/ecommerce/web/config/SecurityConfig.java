package com.api.ecommerce.web.config;

import com.api.ecommerce.web.service.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inyectar el filtro de JSON Web Token
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    // Cadena de filtros para verificar una solicitud
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Desactivar CSRF (recomendado si se usa JWT o solo APIs)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Definir politica de sesión (stateless para APIs)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. Configurar autorización de requests
                .authorizeHttpRequests(auth -> auth
                        // Endpoints publicos
                        .requestMatchers(HttpMethod.POST,"/api/users/auth/*").permitAll()

                        .requestMatchers(HttpMethod.GET,"/api/vehicles/all").permitAll()
                        .requestMatchers(new RegexRequestMatcher("^/api/vehicles/\\d+$", "GET")).permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/vehicles/search").permitAll()

                        .requestMatchers(HttpMethod.GET,"/api/accessories/all").permitAll()
                        .requestMatchers(new RegexRequestMatcher("^/api/accessories/\\d+$", "GET")).permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/accessories/search").permitAll()

                        // Endpoints para gestion administrativa de usuarios
                        .requestMatchers(new RegexRequestMatcher("^/api/users/\\d+$", "PUT")).hasRole("ROOT")
                        .requestMatchers(HttpMethod.GET,"/api/users/all").hasAnyRole("ROOT", "ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users/search/*").hasAnyRole("ROOT", "ADMIN")

                        // Endpoints para gestion administrativa de vehiculos
                        .requestMatchers(HttpMethod.GET,"/api/vehicles/all-including-inactive").hasAnyRole("ROOT", "ADMIN")
                        .requestMatchers(new RegexRequestMatcher("^/api/vehicles/find/\\d+$", "GET")).hasAnyRole("ROOT", "ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/vehicles/create").hasAnyRole("ROOT", "ADMIN")
                        .requestMatchers(new RegexRequestMatcher("^/api/vehicles/\\d+$", "PUT")).hasAnyRole("ROOT", "ADMIN")
                        .requestMatchers(new RegexRequestMatcher("^/api/vehicles/\\d+$", "DELETE")).hasAnyRole("ROOT", "ADMIN")
                        .requestMatchers(new RegexRequestMatcher("^/api/vehicles/restore/\\d+$", "PUT")).hasAnyRole("ROOT", "ADMIN")

                        // Endpoints para gestion administrativa de accesorios para vehiculos
                        .requestMatchers(HttpMethod.GET,"/api/accesories/all-including-inactive").hasAnyRole("ROOT", "ADMIN")
                        .requestMatchers(new RegexRequestMatcher("^/api/accesories/find/\\d+$", "GET")).hasAnyRole("ROOT", "ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/accesories/create").hasAnyRole("ROOT", "ADMIN")
                        .requestMatchers(new RegexRequestMatcher("^/api/accesories/\\d+$", "PUT")).hasAnyRole("ROOT", "ADMIN")
                        .requestMatchers(new RegexRequestMatcher("^/api/accesories/\\d+$", "DELETE")).hasAnyRole("ROOT", "ADMIN")
                        .requestMatchers(new RegexRequestMatcher("^/api/accesories/restore/\\d+$", "PUT")).hasAnyRole("ROOT", "ADMIN")

                        .anyRequest().authenticated()
                )

                // Agregar el filtro de autenticacion por JWTs
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Agregar filtro de autenticacion por medio de JWT antes de la autenticacion por medio de UsernamePasswordAuthenticationFilter.
                //.httpBasic(Customizer.withDefaults()) Eliminamos BasicAuthentication
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
