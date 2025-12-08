package com.wiss.dragonball.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Webkonfiguration für Cross-Origin Resource Sharing (CORS).
 * <p>
 * Diese Klasse konfiguriert die CORS-Richtlinien für die API-Endpunkte
 * unter <code>/api/characters/**</code>. Sie erlaubt den Zugriff vom Vite Dev Server
 * (Frontend unter <code>http://localhost:5173</code>), um die Kommunikation zwischen
 * Frontend und Backend während der Entwicklung zu ermöglichen.
 * </p>
 *
 * <p><strong>Beispiel:</strong> Eine React-App auf Port 5173 kann dadurch
 * auf das Spring Boot Backend auf Port 8080 zugreifen.</p>
 *
 * @author Thierno Hamidou Bah
 * @version 1.0.0
 * @since 2025-07-20
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Konfiguriert CORS für die <code>/api/characters/**</code> Routen.
     * <p>
     * Erlaubt GET, POST, PUT, DELETE und OPTIONS Anfragen vom Vite Dev Server,
     * akzeptiert alle Header und deaktiviert Cookies bzw. Authentifizierung.
     * </p>
     *
     * @param registry das CORS-Registry-Objekt, das die Mappings verwaltet
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/characters/**") // Alle /api/characters-Endpunkte freigeben
                .allowedOrigins("http://localhost:5173") // Vite Dev Server
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // REST-Methoden
                .allowedHeaders("*") // alle Header erlaubt
                .allowCredentials(false) // kein Cookie/Auth (noch)
                .maxAge(3600); // Browser cached die Info für 1 Stunde
    }
}
