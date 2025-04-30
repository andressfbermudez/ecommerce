package com.api.ecommerce.web.controller.exception;

import java.util.Map;
import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@RestControllerAdvice
public class GlobalHandlerException {

    // Para NO permitir el envio de campos no necesarios en el JSON
    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<Map<String, String>> unrecognizedPropertyException(UnrecognizedPropertyException ex) {
        return new ResponseEntity<>(
                Collections.singletonMap("Error", "Attribute not allowed: " + ex.getPropertyName()),
                HttpStatus.BAD_REQUEST
        );
    }

    // Para capturar excepciones de tipo SQL constraint
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> sqlIntegrityConstraintViolationExceptions(SQLIntegrityConstraintViolationException e) {
        return  ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }

    // Para capturar excepciones relacionadas a datos invalidados
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptions(MethodArgumentNotValidException e) {
        // Extraer el último mensaje de error
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .getLast() // Último error
                .getDefaultMessage();
        return ResponseEntity.badRequest().body("Error: " + errorMessage);
    }

    // Este metodo es para capturar las excepciones personalizadas creadas por el desarrollador
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> validationExceptions(ValidationException e) {
        return  ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}
