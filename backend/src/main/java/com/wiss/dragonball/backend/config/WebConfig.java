package com.wiss.dragonball.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Webkonfiguration für Cross-Origin Resource Sharing (CORS).
 *
 * Erlaubt dem Vite-Dev-Server (http://localhost:5173) den Zugriff auf alle
 * Backend-Routen, damit Login (auth/users) und Charakter-Endpunkte
 * funktionieren.
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Development CORS setup for all API routes (auth, users, characters, etc.).
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
