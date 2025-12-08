package com.wiss.dragonball.backend.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Ausnahme, die ausgelöst wird, wenn kein Charakter mit dem angegebenen Power Level gefunden wird.
 * <p>
 * Diese Exception wird verwendet, wenn z.B. eine Suche nach einem bestimmten KI-Wert
 * keinen Treffer in der Datenbank ergibt.
 * </p>
 *
 * <p><b>Hinweis:</b> Diese Klasse ist in der API-Dokumentation (Swagger) ausgeblendet.</p>
 *
 * @author Thierno
 * @version 1.0
 */
@Schema(hidden = true)
public class PowerLevelNotFoundException extends RuntimeException {

    /**
     * Der gesuchte Power Level, der keinen Treffer ergab.
     */
    private final long level;

    /**
     * Konstruktor zur Initialisierung mit dem fehlgeschlagenen Power Level.
     *
     * @param level Der angeforderte, aber nicht gefundene Power Level
     */
    public PowerLevelNotFoundException(long level) {
        super("No character found with power level " + level + ".");
        this.level = level;
    }

    /**
     * Gibt den Power Level zurück, der keinen Treffer in der Datenbank ergab.
     *
     * @return Power Level als long
     */
    public long getLevel() {
        return level;
    }
}
