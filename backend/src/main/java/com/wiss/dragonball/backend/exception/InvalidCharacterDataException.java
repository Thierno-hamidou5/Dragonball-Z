package com.wiss.dragonball.backend.exception;

/**
 * Ausnahme, die ausgelöst wird, wenn ungültige Daten für einen Charakter übermittelt wurden.
 * <p>
 * Diese Exception kann mit oder ohne spezifischen Grund verwendet werden und wird z.B.
 * beim Validieren von Eingaben ausgelöst, wenn die gelieferten Charakterdaten
 * nicht den erwarteten Anforderungen entsprechen.
 * </p>
 *
 * @author Thierno
 * @version 1.0
 */
public class InvalidCharacterDataException extends RuntimeException

    /**
    * Optionaler Grund für die Ungültigkeit der Daten.
    */{
    private final String reason;

    /**
     * Konstruktor mit spezifischem Grund für die Ausnahme.
     *
     * @param reason Beschreibung des Fehlers (z.B. "KI-Wert darf nicht leer sein")
     */
    public InvalidCharacterDataException(String reason) {
        super(reason);
        this.reason = reason;
    }

    /**
     * Konstruktor ohne spezifischen Grund.
     */
    public InvalidCharacterDataException() {
        super();
        this.reason = null;
    }

    /**
     * Gibt den angegebenen Grund für die Ausnahme zurück (falls vorhanden).
     *
     * @return Grund für die Ausnahme oder {@code null}, wenn keiner angegeben wurde
     */
    public String getReason() {
        return reason;
    }
}
