package com.wiss.dragonball.backend.repository;

import com.wiss.dragonball.backend.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository-Interface für den Zugriff auf {@link Character} Entitäten.
 * <p>
 * Bietet CRUD-Operationen sowie benutzerdefinierte Abfragen über Methodennamen
 * mit Spring Data JPA.
 * </p>
 *
 * @author Thierno
 * @version 1.0
 */
public interface CharacterRepository extends JpaRepository<Character, Long> {

    /**
     * Sucht einen Charakter mit exakt übereinstimmendem Namen.
     *
     * @param name Der gesuchte Name
     * @return Optional mit dem Charakter, falls vorhanden
     */
    Optional<Character> findByName(String name);

    /**
     * Findet alle Charaktere mit exakt übereinstimmender Rasse (Groß-/Kleinschreibung beachtet).
     *
     * @param race Die gesuchte Rasse
     * @return Liste der passenden Charaktere
     */
    List<Character> findByRace(String race);

    /**
     * Findet alle Charaktere mit exakt übereinstimmender Rasse (Groß-/Kleinschreibung ignoriert).
     *
     * @param race Die gesuchte Rasse
     * @return Liste der passenden Charaktere
     */
    List<Character> findByRaceIgnoreCase(String race);

    /**
     * Findet alle Charaktere mit genau diesem Power Level.
     *
     * @param powerLevel Der Power Level
     * @return Liste der passenden Charaktere
     */
    List<Character> findByPowerLevel(long powerLevel);

    /**
     * Führt eine unscharfe Suche nach dem Namen durch (z. B. "go" findet "Goku"), Groß-/Kleinschreibung ignoriert.
     *
     * @param name Suchbegriff
     * @return Liste der passenden Charaktere
     */
    List<Character> findByNameContainingIgnoreCase(String name);

    /**
     * Zählt, wie viele Charaktere es mit einer bestimmten Rasse gibt.
     *
     * @param race Die Rasse
     * @return Anzahl der Charaktere mit dieser Rasse
     */
    long countByRace(String race);

    /**
     * Findet die Top 5 Charaktere mit einem Power Level größer als {@code minPower}, sortiert absteigend.
     *
     * @param minPower Untergrenze für das Power Level
     * @return Liste der stärksten 5 Charaktere über der Grenze
     */
    List<Character> findTop5ByPowerLevelGreaterThanOrderByPowerLevelDesc(int minPower);

    /**
     * Prüft, ob ein Charakter mit einem bestimmten Namen und einer bestimmten Rasse existiert.
     *
     * @param name Der Name
     * @param race Die Rasse
     * @return {@code true}, wenn mindestens ein passender Charakter existiert
     */
    boolean existsByNameAndRace(String name, String race);
}
