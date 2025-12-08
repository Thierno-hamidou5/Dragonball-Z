package com.wiss.dragonball.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.time.ZoneId;


/**
 * Einheitliches Data Transfer Object (DTO) für Fehlerantworten der Dragonball REST API.
 * <p>
 * Dieses Objekt wird verwendet, um strukturierte Fehlermeldungen an den Client zu senden.
 * Es enthält Informationen über den HTTP-Status, den Fehlertext, den Pfad der Anfrage
 * und einen Zeitstempel.
 * </p>
 *
 * <p><strong>Beispielausgabe:</strong></p>
 * <pre>{@code
 * {
 *   "error": "Bad Request",
 *   "message": "Character not found",
 *   "status": 400,
 *   "path": "/api/characters/99",
 *   "timestamp": "2025-07-20T14:52:37+02:00"
 * }
 * }</pre>
 *
 * @author Thierno
 * @version 1.0
 * @since 2025-07-20
 * @see com.wiss.dragonball.backend.exception.GlobalExceptionHandler
 */
public class ErrorResponseDTO {

    /**
     * Kurzer Fehlername entsprechend dem HTTP-Fehler (z.B. "Bad Request").
     */
    private final String error;

    /**
     * Ausführlichere Beschreibung oder benutzerdefinierte Fehlermeldung.
     */
    private final String message;

    /**
     * HTTP-Statuscode (z.B. 404, 400, 500).
     */
    private final int status;

    /**
     * Pfad der Anfrage, bei der der Fehler aufgetreten ist.
     */
    private final String path;

    /**
     * Zeitpunkt, zu dem der Fehler generiert wurde.
     * <p>
     * Das Format ist ISO 8601 und die Zeitzone ist Europa/Zurich.
     * </p>
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Europe/Zurich")
    private final OffsetDateTime timestamp;

    /**
     * Erstellt eine neue Fehlerantwort.
     *
     * @param error   Der HTTP-Fehlertyp (z.B. "Not Found")
     * @param message Die detaillierte Fehlermeldung
     * @param status  Der HTTP-Statuscode
     * @param path    Der Pfad der aufgerufenen URL
     */
    public ErrorResponseDTO(String error, String message, int status, String path) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = OffsetDateTime.now(ZoneId.of("Europe/Zurich"));
    }

// Getter
    /**
     * Gibt die Art des Fehlers zurück (z.B. "Not Found", "Bad Request").
     */
    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }
}
