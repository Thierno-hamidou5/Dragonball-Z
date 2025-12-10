package com.wiss.dragonball.backend.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * JPA-Entity fÃ¼r einen Dragon Ball Charakter.
 * <p>
 * Diese Klasse wird verwendet, um Charakterdaten in der Datenbank zu speichern und abzurufen.
 * Sie bildet die Datenstruktur der Tabelle <code>character</code> ab.
 * </p>
 *
 * @author Thierno Hamidou Bah
 * @version 1.0
 * @since 2025-07-20
 */

/**
 * Schweizerdeutsche Kurzbeschreibung: persistente Darstellung eines Dragonball-Charakters.
 * Haelt alle Felder, die das REST-API speichert, inklusive Bilder, Rasse und Transformationen.
 */
@Entity
@Table(name = "character")
public class Character {

    /**
     * Eindeutige ID des Charakters (PrimÃ¤rschlÃ¼ssel).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name des Charakters (z.B. Goku, Vegeta).
     */
    private String name;

    /**
     * Rasse des Charakters (z.B. Saiyan, Android, Namekian).
     */
    private String race;

    /**
     * Aktuelles Ki-Level (als Text fÃ¼r formatierten Wert, z.B. "60,000,000").
     */
    private String ki;

    /**
     * Maximales Ki-Level (z.B. "90 Septillion").
     */
    private String maxKi;

    /**
     * Power Level des Charakters (z.B. 900000000000).
     */
    private long powerLevel;

    /**
     * Universum, zu dem der Charakter gehÃ¶rt (1 bis 12).
     */
    private int universe;

    /**
     * Gibt an, ob der Charakter ein BÃ¶sewicht ist.
     */
    private boolean isVillain;

    /**
     * Geschlecht des Charakters (z.B. Male, Female).
     */
    private String gender;

    /**
     * Kurze Beschreibung Ã¼ber den Charakter.
     */
    private String description;

    /**
     * URL zum Hauptbild des Charakters.
     */
    private String image;

    /**
     * ZugehÃ¶rigkeit oder Team (z.B. Z Fighter, Frieza Force).
     */
    private String affiliation;


    /**
     * Liste der Transformationen (z.B. Super Saiyan, Ultra Instinct).
     */
    @ElementCollection
    private List<String> transformations;

    /**
     * Alternative Bild-URL oder Zusatzbild.
     */
    private String imageUrl;

    /**
     * Leerer Konstruktor fÃ¼r JPA.
     */
    public Character() {
    }

    /**
     * Konstruktor ohne ID, z.B. fÃ¼r Erstellung eines neuen Charakters.
     */
    public Character(String name, String race, String ki, String maxKi, long powerLevel,
                     int universe, boolean isVillain,
                     List<String> transformations, String imageUrl,
                     String gender, String description, String image, String affiliation) {
        this.name = name;
        this.race = race;
        this.ki = ki;
        this.maxKi = maxKi;
        this.powerLevel = powerLevel;
        this.universe = universe;
        this.isVillain = isVillain;
        this.transformations = transformations;
        this.imageUrl = (imageUrl == null || imageUrl.isBlank())
                ? "https://static.wikia.nocookie.net/dragonball/images/f/f8/Jiren_DBZ_Episode_127.png"
                : imageUrl;
        this.gender = gender;
        this.description = description;
        this.image = image;
        this.affiliation = affiliation;
    }

    /**
     * Konstruktor mit ID, z.B. fÃ¼r Update oder DB-Import.
     */
    public Character(Long id, String name, String race, String ki, String maxKi, long powerLevel,
                     int universe, boolean isVillain,
                     List<String> transformations, String imageUrl,
                     String gender, String description, String image, String affiliation) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.ki = ki;
        this.maxKi = maxKi;
        this.powerLevel = powerLevel;
        this.universe = universe;
        this.isVillain = isVillain;
        this.transformations = transformations;
        this.imageUrl = (imageUrl == null || imageUrl.isBlank())
                ? "https://static.wikia.nocookie.net/dragonball/images/f/f8/Jiren_DBZ_Episode_127.png"
                : imageUrl;
        this.gender = gender;
        this.description = description;
        this.image = image;
        this.affiliation = affiliation;
    }

    // ðŸ§© Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }

    public String getKi() { return ki; }
    public void setKi(String ki) { this.ki = ki; }

    public String getMaxKi() { return maxKi; }
    public void setMaxKi(String maxKi) { this.maxKi = maxKi; }

    public long getPowerLevel() { return powerLevel; }
    public void setPowerLevel(long powerLevel) { this.powerLevel = powerLevel; }

    public int getUniverse() { return universe; }
    public void setUniverse(int universe) { this.universe = universe; }

    public boolean isVillain() { return isVillain; }
    public void setVillain(boolean isVillain) { this.isVillain = isVillain; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getAffiliation() { return affiliation; }
    public void setAffiliation(String affiliation) { this.affiliation = affiliation; }

    public List<String> getTransformations() { return transformations; }
    public void setTransformations(List<String> transformations) { this.transformations = transformations; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }


}

