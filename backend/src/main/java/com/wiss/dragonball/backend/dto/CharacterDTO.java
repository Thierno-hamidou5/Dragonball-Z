package com.wiss.dragonball.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) fÃ¼r Dragonball-Charaktere.
 * <p>
 * Dieses DTO wird verwendet, um Charakterdaten zwischen Backend und Frontend auszutauschen.
 * Es enthÃ¤lt Validierungsregeln sowie Beschreibungen fÃ¼r Swagger/OpenAPI.
 * </p>
 *
 * <p><strong>Verwendung:</strong></p>
 * <pre>{@code
 * CharacterDTO goku = new CharacterDTO();
 * goku.setName("Goku");
 * goku.setPowerLevel(900000000000L);
 * }</pre>
 *
 * @author Thierno
 * @version 1.0
 * @since 2025-07-20
 */
@Schema(description = "DTO for Dragon Ball character data")

/**
 * Schweizerdeutsche Zusammenfassung: Transportobjekt fuer Charakterdaten zwischen API und Client.
 * HÃ¤lt Validierungsregeln bereit und dient als neutrales Format ohne JPA-Abhaengigkeiten.
 */
public class CharacterDTO {

    /**
     * Eindeutige ID des Charakters.
     * <p>Wird automatisch generiert und bei neuen Charakteren null.</p>
     */
    @Schema(description = "Unique identifier of the character", example = "1")
    private Long id;

    /**
     * Name des Charakters (z.B. Goku).
     * <p>Darf nicht leer sein.</p>
     */
    @NotBlank(message = "Name darf nicht leer sein")
    @Schema(description = "Name of the character", example = "Goku")
    private String name;

    /**
     * Aktuelles Ki-Level (als String z.B. "60,000,000").
     */
    @Schema(description = "Current Ki level", example = "60,000,000")
    private String ki;

    /**
     * Maximal erreichbares Ki (als String z.B. "90 Septillion").
     */
    @Schema(description = "Maximum Ki level", example = "90 Septillion")
    private String maxKi;

    /**
     * Rasse des Charakters (z.B. Saiyan, Namekian, Android).
     */
    @Schema(description = "Race of the character", example = "Saiyan")
    private String race;

    /**
     * Geschlecht des Charakters (z.B. Male, Female).
     */
    @Schema(description = "Gender of the character", example = "Male")
    private String gender;

    /**
     * Kurze Beschreibung zum Charakter.
     * <p>
     * Kann Informationen zur Rolle, Hintergrundgeschichte oder Eigenschaften enthalten.
     * </p>
     */
    @Schema(description = "Short description of the character", example = "The main protagonist of the series...")
    private String description;

    /**
     * URL zum Hauptbild des Charakters.
     */
    @Schema(description = "Main image URL of the character", example = "https://dragonball-api.com/characters/goku_normal.webp")
    private String image;

    /**
     * ZugehÃ¶rigkeit oder Team des Charakters (z.B. Z Fighter, Frieza Force).
     */
    @Schema(description = "Affiliation or team", example = "Z Fighter")
    private String affiliation;

    /**
     * GeschÃ¤tzter Power Level des Charakters.
     * <p>
     * Wird als Long gespeichert, um sehr grosse Werte zu ermÃ¶glichen.
     * </p>
     */
    @Schema(description = "Estimated power level", example = "900000000000", type = "integer", format = "int64")
    private long powerLevel;

    /**
     * Universum, dem der Charakter angehÃ¶rt (1 bis 12).
     */
    @Schema(description = "Universe the character belongs to", example = "7")
    private int universe;

    /**
     * Kennzeichnet, ob es sich um einen BÃ¶sewicht handelt.
     * <p>
     * Wird im JSON als <code>villain: true/false</code> dargestellt.
     * </p>
     */
    @Schema(description = "True if the character is a villain", example = "false")
    private boolean villain;  // umbenannt von isVillain, um Swagger-Probleme zu vermeiden

    /**
     * Liste der Transformationen des Charakters.
     * <p>
     * Beispiele: Super Saiyan, Ultra Instinct, Golden Frieza.
     * </p>
     */
    @Schema(description = "List of transformations (e.g., Super Saiyan, Ultra Ego)", example = "[\"Super Saiyan\", \"SSGSS\"]")
    private List<String> transformations = new ArrayList<>();  // vorinitialisiert

    /**
     * ZusÃ¤tzliche oder alternative Bild-URL zum Charakter.
     */
    @Schema(description = "Alternative or extra image URL", example = "https://example.com/vegeta_alternate.png")
    private String imageUrl;

    /**
     * Leerer Konstruktor fÃ¼r das Framework und die Serialisierung.
     */
    public CharacterDTO() {}

    /**
     * Konstruktor zur Initialisierung eines vollstÃ¤ndigen CharacterDTO-Objekts.
     * <p>
     * Wird verwendet, um alle Eigenschaften eines Charakters manuell zu setzen.
     * Falls die Transformationen {@code null} sind, wird automatisch eine leere Liste gesetzt.
     * </p>
     *
     * @param id             Eindeutige ID
     * @param name           Name des Charakters
     * @param ki             Aktueller KI-Wert
     * @param maxKi          Maximaler KI-Wert
     * @param race           Rasse des Charakters
     * @param gender         Geschlecht
     * @param description    Beschreibung
     * @param image          Bild-URL
     * @param affiliation    ZugehÃ¶rigkeit (z.B. Z-Fighter)
     * @param powerLevel     Kampfkraft
     * @param universe       Universum
     * @param villain        true, wenn BÃ¶sewicht
     * @param transformations Liste der Transformationen
     * @param imageUrl       Alternativbild
     */
    public CharacterDTO(Long id, String name, String ki, String maxKi, String race,
                        String gender, String description, String image, String affiliation,
                        long powerLevel, int universe, boolean villain,
                        List<String> transformations, String imageUrl) {
        this.id = id;
        this.name = name;
        this.ki = ki;
        this.maxKi = maxKi;
        this.race = race;
        this.gender = gender;
        this.description = description;
        this.image = image;
        this.affiliation = affiliation;
        this.powerLevel = powerLevel;
        this.universe = universe;
        this.villain = villain;
        this.transformations = transformations != null ? transformations : new ArrayList<>();
        this.imageUrl = imageUrl;
    }

    /**
     * Getter- und Setter-Methoden fÃ¼r alle Felder des DTOs.
     * <p>
     * Dienen der Kapselung und erlauben den kontrollierten Zugriff auf die Eigenschaften
     * des Charakters (z.B. Name, Rasse, Kampfkraft etc.).
     * </p>
     */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getKi() { return ki; }
    public void setKi(String ki) { this.ki = ki; }

    public String getMaxKi() { return maxKi; }
    public void setMaxKi(String maxKi) { this.maxKi = maxKi; }

    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getAffiliation() { return affiliation; }
    public void setAffiliation(String affiliation) { this.affiliation = affiliation; }

    public long getPowerLevel() { return powerLevel; }
    public void setPowerLevel(long powerLevel) { this.powerLevel = powerLevel; }

    public int getUniverse() { return universe; }
    public void setUniverse(int universe) { this.universe = universe; }

    public boolean isVillain() { return villain; }
    public void setVillain(boolean villain) { this.villain = villain; }

    public List<String> getTransformations() { return transformations; }
    public void setTransformations(List<String> transformations) {
        this.transformations = transformations != null ? transformations : new ArrayList<>();
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

