package com.wiss.dragonball.backend.service;

import com.wiss.dragonball.backend.dto.CharacterDTO;
import com.wiss.dragonball.backend.entity.Character;
import com.wiss.dragonball.backend.exception.*;
import com.wiss.dragonball.backend.mapper.CharacterMapper;
import com.wiss.dragonball.backend.repository.CharacterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service-Klasse für die Verwaltung von Dragonball-Charakteren.
 * <p>
 * Diese Klasse enthält die Geschäftslogik für das Erstellen, Lesen,
 * Aktualisieren und Löschen (CRUD) von Charakteren sowie
 * verschiedene Abfragen nach Name, Rasse und Powerlevel.
 * </p>
 *
 * @author Thierno
 * @version 1.0
 * @since 2025-07-20
 */
@Service
public class CharacterService {

    /**
     * Repository für den Datenbankzugriff auf Charaktere.
     */
    private final CharacterRepository repository;

    public CharacterService(CharacterRepository repository) {
        this.repository = repository;
    }

    /**
     * Ruft einen Charakter anhand seines Namens ab.
     *
     * @param name Der Name des gesuchten Charakters
     * @return Der gefundene Charakter als DTO
     * @throws CharacterNotFoundException wenn kein Charakter gefunden wird
     * @throws IllegalArgumentException wenn der Name leer oder null ist
     */
    public CharacterDTO getCharacterByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Character name must not be empty");
        }

        Character character = repository.findByName(name)
                .orElseThrow(() -> new CharacterNotFoundException(name));

        return CharacterMapper.toDTO(character);
    }

    /**
     * Ruft einen Charakter anhand seiner ID ab.
     *
     * @param id Die ID des Charakters
     * @return Der gefundene Charakter
     * @throws CharacterNotFoundException wenn kein Charakter mit der ID gefunden wird
     */
    public CharacterDTO getCharacterById(Long id) {
        Character character = repository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException("Character with ID: " + id + " not found"));
        return CharacterMapper.toDTO(character);
    }

    /**
     * Ruft alle Charaktere einer bestimmten Rasse ab.
     *
     * @param race Die gesuchte Rasse (z.B. "Saiyan")
     * @return Liste aller passenden Charaktere
     * @throws RaceNotFoundException wenn keine Charaktere gefunden wurden
     */
    public List<CharacterDTO> getCharactersByRace(String race) {
        validateRace(race);
        List<Character> characters = repository.findByRace(race);
        if (characters.isEmpty()) {
            throw new RaceNotFoundException(race);
        }
        return CharacterMapper.toDTOList(characters);
    }

    /**
     * Wie {@link #getCharactersByRace(String)}, aber ohne Beachtung der Gross-/Kleinschreibung.
     *
     * @param race Die gesuchte Rasse
     * @return Liste der Charaktere
     * @throws RaceNotFoundException wenn keine passenden gefunden werden
     */
    public List<CharacterDTO> getCharactersByRaceIgnoreCase(String race) {
        validateRace(race);
        List<Character> characters = repository.findByRaceIgnoreCase(race);
        if (characters.isEmpty()) {
            throw new RaceNotFoundException(race);
        }
        return CharacterMapper.toDTOList(characters);
    }

    /**
     * Ruft Charaktere mit einem bestimmten Powerlevel ab.
     *
     * @param powerLevel Der gewünschte Powerlevel
     * @return Liste der Charaktere mit genau diesem Level
     * @throws PowerLevelNotFoundException wenn keine Charaktere gefunden wurden
     */
    public List<CharacterDTO> getCharactersByPowerLevel(long powerLevel) {
        if (powerLevel < 0) {
            throw new IllegalArgumentException("Power level must be positive");
        }

        List<Character> result = repository.findByPowerLevel(powerLevel);
        if (result.isEmpty()) {
            throw new PowerLevelNotFoundException(powerLevel);
        }

        return CharacterMapper.toDTOList(result);
    }

    /**
     * Sucht Charaktere anhand eines Teilstrings im Namen.
     *
     * @param nameFragment Teil des Namens
     * @return Gefundene Charaktere
     * @throws CharacterNotFoundException wenn keine Übereinstimmung gefunden wurde
     */
    public List<CharacterDTO> searchByName(String nameFragment) {
        if (nameFragment == null || nameFragment.isBlank()) {
            throw new IllegalArgumentException("Search term must not be empty");
        }

        List<Character> result = repository.findByNameContainingIgnoreCase(nameFragment);
        if (result.isEmpty()) {
            throw new CharacterNotFoundException("No character found for: " + nameFragment);
        }

        return CharacterMapper.toDTOList(result);
    }

    /**
     * Zählt, wie viele Charaktere es für eine bestimmte Rasse gibt.
     *
     * @param race Die gesuchte Rasse
     * @return Anzahl der Charaktere
     */
    public long countCharactersByRace(String race) {
        validateRace(race);
        return repository.countByRace(race);
    }

    /**
     * Gibt die fünf stärksten Charaktere mit einem Powerlevel grösser als minPower zurück.
     *
     * @param minPower Minimaler Powerlevel
     * @return Liste der Top-5-Charaktere
     */
    public List<CharacterDTO> getTop5ByPowerGreaterThan(int minPower) {
        if (minPower < 0) {
            throw new IllegalArgumentException("Minimum power level must be positive");
        }

        List<Character> top5 = repository.findTop5ByPowerLevelGreaterThanOrderByPowerLevelDesc(minPower);
        return CharacterMapper.toDTOList(top5);
    }

    /**
     * Prüft, ob ein Charakter mit dem gegebenen Namen und Rasse existiert.
     *
     * @param name Der Name
     * @param race Die Rasse
     * @return true, wenn ein solcher Charakter existiert
     */
    public boolean existsByNameAndRace(String name, String race) {
        if (name == null || race == null || name.isBlank() || race.isBlank()) {
            throw new IllegalArgumentException("Name and race must not be empty");
        }

        validateRace(race);
        return repository.existsByNameAndRace(name, race);
    }

    /**
     * Erstellt einen neuen Charakter.
     *
     * @param dto Daten des zu erstellenden Charakters
     * @return Der gespeicherte Charakter als DTO
     * @throws InvalidCharacterDataException bei ungültigen Daten
     */
    public CharacterDTO createCharacter(CharacterDTO dto) {
        validateCharacterData(dto);
        Character entity = CharacterMapper.toEntity(dto);
        Character saved = repository.save(entity);
        return CharacterMapper.toDTO(saved);
    }

    /**
     * Aktualisiert einen existierenden Charakter anhand seiner ID.
     *
     * @param id  Die ID des zu aktualisierenden Charakters
     * @param dto Neue Daten
     * @return Der aktualisierte Charakter
     * @throws CharacterNotFoundException wenn der Charakter nicht existiert
     */
    public CharacterDTO updateCharacter(Long id, CharacterDTO dto) {
        if (!repository.existsById(id)) {
            throw new CharacterNotFoundException("ID " + id);
        }

        validateCharacterData(dto);
        Character entity = CharacterMapper.toEntity(dto);
        entity.setId(id);
        Character updated = repository.save(entity);
        return CharacterMapper.toDTO(updated);
    }

    /**
     * Löscht einen Charakter anhand seiner ID.
     *
     * @param id Die ID des Charakters
     * @throws CharacterNotFoundException wenn der Charakter nicht existiert
     */
    public void deleteCharacter(Long id) {
        if (!repository.existsById(id)) {
            throw new CharacterNotFoundException(id);

        }
        repository.deleteById(id);
    }

    /**
     * Gibt alle gespeicherten Charaktere zurück.
     *
     * @return Liste aller Charaktere
     */
    public List<CharacterDTO> getAllCharacters() {
        return repository.findAll()
                .stream()
                .map(CharacterMapper::toDTO)
                .toList();
    }

    // 🔒 Private Hilfsmethoden

    /**
     * Validiert die Daten eines Charakters.
     *
     * @param dto Der zu prüfende Charakter
     * @throws InvalidCharacterDataException bei ungültigen Feldern
     */
    private void validateCharacterData(CharacterDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new InvalidCharacterDataException("Name is required");
        }
        if (dto.getRace() == null || dto.getRace().isBlank()) {
            throw new InvalidCharacterDataException("Race is required");
        }
        if (dto.getPowerLevel() < 0) {
            throw new InvalidCharacterDataException("Power level must be positive");
        }
    }

    /**
     * Prüft, ob die angegebene Rasse erlaubt ist.
     *
     * @param race Die zu prüfende Rasse
     * @throws RaceNotFoundException wenn die Rasse nicht bekannt ist
     */
    private void validateRace(String race) {
        List<String> validRaces = List.of("saiyan", "namekian", "android", "human", "god", "frieza", "majin");
        if (race == null || race.isBlank()) {
            throw new IllegalArgumentException("Race must not be empty");
        }
        if (!validRaces.contains(race.toLowerCase())) {
            throw new RaceNotFoundException(race);
        }
    }
}
