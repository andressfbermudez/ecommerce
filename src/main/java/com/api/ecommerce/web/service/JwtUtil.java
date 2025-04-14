package com.api.ecommerce.web.service;

import java.util.Date;
import com.auth0.jwt.JWT;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.PostConstruct;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtUtil {

     // El token expirará 15 días después de ser creado.
     // Convierte 15 días a milisegundos y se los suma al tiempo actual.
     private final Date EXPIRED_DATE = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15));

     // SECRET_KEY viene del application.properties.
     @Value("jwt_secret")
     private String SECRET_KEY;

     // Crear el algoritmo de firma con HMAC y la clave secreta (SECRET_KEY),
     // necesaria para firmar y luego verificar el JWT.
     private Algorithm ALGORITHM;

     /* @PostConstruct indica a Spring que ese método debe ejecutarse después de que se haya construido
      * el bean y se hayan inyectado todas sus dependencias, como @Value, @Autowired, etc.
      *
      * En Java, cuando se crea un objeto:
      *
      * Se inicializan los campos directamente asignados (como private Algorithm algo = ...).
      * Luego se ejecuta el constructor.
      * Después se puede hacer algo extra (si se programa).
      *
      * 🔧 Spring (inyección de dependencias)
      * Spring sigue este flujo cuando crea un bean:
      *
      * Crea la instancia del objeto (constructor sin parámetros o con dependencias).
      * Inyecta valores con @Autowired, @Value, etc.
      * Luego ejecuta métodos anotados con @PostConstruct.
      *
      * Entonces, si usamos un campo como: private final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
      * Eso se ejecuta antes de que Spring haya inyectado SECRET_KEY, así que da null.
     */
     @PostConstruct // Para que se inicialice despues de cargar el SECRET_KEY.
     public void init() {
          this.ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
     }

     // Metodo que recibe un email y devuelve un JWT (tipo String).
     public String create(String usernameOrEmail) {
         return JWT.create()
                 .withSubject(usernameOrEmail) // Asignar el subject del token, que suele ser el usuario o ID principal.
                 .withIssuer("ecommerce_api") // Definir quién emite el token
                 .withIssuedAt(new Date()) // Marcar la fecha/hora de emisión del token.
                 .withExpiresAt(EXPIRED_DATE) // Fecha de expiración del token (debe ser un Date).
                 .sign(ALGORITHM); //Firma el token con un algoritmo (por ejemplo, HMAC256 con la clave secreta).
     }

     // Metodo para validar si un JWT es valido, es decir, que no ha sido modificado, o la fecha
     // de expiracion es valida, etc.
     public Boolean isValid(String jwt) {
          try {
               JWT.require(ALGORITHM)
                       .build()
                       .verify(jwt);
               return true;
          } catch (JWTVerificationException e) {
               return false;
          }
     }

     // Metodo para obtener el usuario del JWT validado.
     public String getSubject(String jwt) {
          return JWT.require(ALGORITHM)
                  .build()
                  .verify(jwt)
                  .getSubject();
     }
}
