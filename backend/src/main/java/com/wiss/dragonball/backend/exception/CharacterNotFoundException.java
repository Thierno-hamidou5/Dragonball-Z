package com.wiss.dragonball.backend.exception;

/**
 * Exception, die geworfen wird, wenn ein gesuchter Charakter nicht gefunden wird.
 * <p>
 * Diese Exception wird verwendet, wenn ein Charakter anhand seines Namens oder seiner ID
 * nicht in der Datenbank gefunden werden kann.
 * </p>
 *
 * @author Thierno
 * @version 1.1
 */
public class CharacterNotFoundException extends RuntimeException {

    /**
     * Optional: Name des gesuchten Charakters.
     */
    private final String characterName;

    /**
     * Optional: ID des gesuchten Charakters.
     */
    private final Long characterId;

    /**
     * Konstruktor für einen nicht gefundenen Charakter anhand des Namens.
     *
     * @param characterName Der gesuchte Name
     */
    public CharacterNotFoundException(String characterName) {
        super("Character with the name '" + characterName + "' was not found");
        this.characterName = characterName;
        this.characterId = null;
    }

    /**
     * Konstruktor für einen nicht gefundenen Charakter anhand der ID.
     *
     * @param characterId Die gesuchte ID
     */
    public CharacterNotFoundException(Long characterId) {
        super("Character with ID: " + characterId + " not found");
        this.characterId = characterId;
        this.characterName = null;
    }

    /**
     * Gibt den Namen zurück, nach dem gesucht wurde (falls gesetzt).
     *
     * @return Name des Charakters oder {@code null}
     */
    public String getCharacterName() {
        return characterName;
    }

    /**
     * Gibt die ID zurück, nach der gesucht wurde (falls gesetzt).
     *
     * @return ID des Charakters oder {@code null}
     */
    public Long getCharacterId() {
        return characterId;
    }
}
