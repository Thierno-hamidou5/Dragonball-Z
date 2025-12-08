package com.wiss.dragonball.backend.exception;

/**
 * Ausnahme, die ausgelöst wird, wenn eine angegebene Rasse nicht in der Datenbank gefunden wurde.
 * <p>
 * Diese Exception wird typischerweise verwendet, wenn ein Benutzer eine ungültige oder nicht vorhandene
 * Rasse angibt, z.B. beim Erstellen oder Filtern von Charakteren.
 * </p>
 *
 * @author Thierno
 * @version 1.0
 */
public class RaceNotFoundException extends RuntimeException {

    /**
     * Die gesuchte Rasse, die nicht gefunden wurde.
     */
    private final String race;

    /**
     * Konstruktor zur Initialisierung mit dem Namen der nicht gefundenen Rasse.
     *
     * @param race Die angegebene, aber ungültige oder nicht vorhandene Rasse
     */
    public RaceNotFoundException(String race) {
        super("Race '" + race + "' not found");
        this.race = race;
    }

    /**
     * Gibt die Rasse zurück, die nicht gefunden wurde.
     *
     * @return Der Name der Rasse
     */
    public String getRace() {
        return race;
    }
}
