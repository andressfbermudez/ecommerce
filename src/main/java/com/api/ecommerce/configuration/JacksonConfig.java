package com.api.ecommerce.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule()) // Soporte para LocalDateTime
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Para serializar en formato ISO
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }
}

