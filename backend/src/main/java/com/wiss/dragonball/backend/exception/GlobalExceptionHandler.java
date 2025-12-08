package com.wiss.dragonball.backend.exception;

import com.wiss.dragonball.backend.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Globale Fehlerbehandlung für REST-Endpunkte der Anwendung.
 * <p>
 * Diese Klasse fängt spezifische und allgemeine Ausnahmen ab und wandelt sie
 * in strukturierte Fehlerantworten ({@link ErrorResponseDTO}) um, die für den Client verständlich sind.
 * </p>
 *
 * @author Thierno
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Behandelt Validierungsfehler bei ungültigen Anfragen (z.B. durch {@code @Valid}).
     *
     * @param ex      Die ausgelöste Validierungs-Ausnahme
     * @param request Die Webanfrage, die den Fehler verursacht hat
     * @return Antwort mit Fehlerdetails und Status 400
     */    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        StringBuilder message = new StringBuilder("Validation error: ");
        errors.forEach((field, error) ->
                message.append(field).append(" - ").append(error).append("; ")
        );

        ErrorResponseDTO error = new ErrorResponseDTO(
                "VALIDATION_ERROR",
                message.toString(),
                400,
                extractPath(request)
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Behandelt unerwartete, allgemeine Ausnahmen.
     *
     * @param ex      Die Ausnahme
     * @param request Die Webanfrage
     * @return Antwort mit allgemeiner Fehlermeldung und Status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(
            Exception ex, WebRequest request) {

        ex.printStackTrace(); // Zum Debuggen

        ErrorResponseDTO error = new ErrorResponseDTO(
                "INTERNAL_SERVER_ERROR",
                "An unexpected internal error occurred. Please try again later or contact support.",
                500,
                extractPath(request)
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Behandelt den Fall, dass eine Rasse nicht gefunden wurde.
     *
     * @param ex      Die {@link RaceNotFoundException}
     * @param request Die HTTP-Anfrage
     * @return Antwort mit Fehlercode und Status 404
     */
    @ExceptionHandler(RaceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRaceNotFound(
            RaceNotFoundException ex, HttpServletRequest request) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                "RACE_NOT_FOUND",
                "The specified race '" + ex.getRace() + "' does not exist in the database.",
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Behandelt ungültige Charakterdaten.
     *
     * @param ex      Die {@link InvalidCharacterDataException}
     * @param request Die HTTP-Anfrage
     * @return Antwort mit Erklärung und Status 400
     */
    @ExceptionHandler(CharacterNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCharacterNotFound(CharacterNotFoundException ex, HttpServletRequest request) {
        String message;
        if (ex.getCharacterName() != null) {
            message = "The character with the name '" + ex.getCharacterName() + "' was not found.";
        } else if (ex.getCharacterId() != null) {
            message = "The character with the ID '" + ex.getCharacterId() + "' was not found.";
        } else {
            message = "Character not found.";
        }

        ErrorResponseDTO error = new ErrorResponseDTO(
                "CHARACTER_NOT_FOUND",
                message,
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }



    /**
     * Behandelt den Fall, dass kein Charakter mit dem angegebenen Power Level gefunden wurde.
     *
     * @param ex      Die {@link PowerLevelNotFoundException}
     * @param request Die HTTP-Anfrage
     * @return Antwort mit spezifischer Fehlermeldung und Status 404
     */
    @ExceptionHandler(InvalidCharacterDataException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCharacterData(
            InvalidCharacterDataException ex, HttpServletRequest request) {

        String reason = ex.getReason();
        String message = (reason != null && !reason.isBlank())
                ? "Invalid character data: " + reason
                : "Invalid character data was submitted.";

        ErrorResponseDTO error = new ErrorResponseDTO(
                "INVALID_CHARACTER_DATA",
                message,
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Extrahiert den URI-Pfad aus einer {@link WebRequest}.
     *
     * @param request Die Webanfrage
     * @return Der angeforderte Pfad (z.B. {@code /api/characters/1})
     */
    @ExceptionHandler(PowerLevelNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlePowerLevelNotFound(
            PowerLevelNotFoundException ex, HttpServletRequest request) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                "POWER_LEVEL_NOT_FOUND",
                "No character found with the power level '" + ex.getLevel() + "'.",
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // ✅ Pfad extrahieren
    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
