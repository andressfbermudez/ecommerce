package com.api.ecommerce.web.service;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserSecurityService userSecurityService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, UserSecurityService userSecurityService) {
        this.jwtUtil = jwtUtil;
        this.userSecurityService = userSecurityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Obtener el JWT enviado en la solicitud en el encabezado AUTHORIZATION.
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Verificar si el header AUTHORIZATION viene vacio.
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            // Si el header AUTHORIZATION viene vacio, no cargaremos nada dentro del contexto
            // de seguridad de Spring, es decir el filterChain continua ejecutandose sin
            // contexto de seguridad, por lo tanto la solicitud sera invalida.
            filterChain.doFilter(request, response);
            return;
        }

        // Verificar si el JWT es valido.
        // .split()[1] genera un arreglo con las dos partes del JWT, en la posicion 0 queda la
        // palabra "Bearer" y en la posicion 1, el JWT.
        String jwt = authorizationHeader.split(" ")[1].trim(); // Obtener el JWT separado de la palabra Bearer.

        // Si el JWT NO es valido no cargaremos nada dentro del contexto
        // de seguridad de Spring, es decir el filterChain continua ejecutandose sin
        // contexto de seguridad, por lo tanto la solicitud sera invalida.
        if (!jwtUtil.isValid(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener el subject que viene dentro del JWT.
        String subject = jwtUtil.getSubject(jwt);

        // Cargar el usuario desde el UserDetailsService.
        User user = (User) userSecurityService.loadUserByUsername(subject);

        // Cargar el usuario en el contexto de seguridad buscandolo en el repositorio
        // de usuarios por medio del subject del JWT.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                // Se carga el usuario ya autenticado
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword(), user.getAuthorities());

        //Cargar el usuario en el contexto de seguridad.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response); // Continua el filterChain con el contexto de seguridad cargado.
    }
}
