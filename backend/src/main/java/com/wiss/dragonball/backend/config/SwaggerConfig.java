package com.wiss.dragonball.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfigurationsklasse für Swagger/OpenAPI.
 * <p>
 * Diese Klasse stellt die OpenAPI-Dokumentation für die REST-API
 * der Dragonball-Charakterverwaltung bereit. Sie definiert Metadaten
 * wie Titel, Beschreibung, Version und Kontaktinformationen für die API.
 * </p>
 *
 * <p><strong>Beispiel-Endpunkte:</strong></p>
 * <ul>
 *     <li><code>/api/characters</code> – Alle Charaktere abrufen</li>
 *     <li><code>/api/characters/{id}</code> – Charakter nach ID abrufen</li>
 * </ul>
 *
 * @author Thierno Hamidou Bah
 * @version 1.0.0
 * @since 2025-07-20
 */

@Configuration
public class SwaggerConfig {

    /**
     * Erstellt und konfiguriert das {@link OpenAPI} Objekt für Swagger UI.
     * <p>
     * Enthält grundlegende API-Informationen wie Titel, Version, Beschreibung
     * und Kontakt für die automatisch generierte Swagger-Dokumentation.
     * </p>
     *
     * @return ein vollständig konfiguriertes {@link OpenAPI} Objekt
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Dragonball Character API")
                        .version("1.0.0")
                        .description("REST API für alle Dragonball Charaktere mit KI-Werten, Rasse, Beschreibung und mehr.")
                        .contact(new Contact()
                                .name("Thierno Hamidou Bah")
                                .email("dragonball@wiss.ch")));
    }
}
