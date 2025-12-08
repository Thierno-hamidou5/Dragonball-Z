package com.wiss.dragonball.backend.controller;

import com.wiss.dragonball.backend.dto.CharacterDTO;
import com.wiss.dragonball.backend.service.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Dragon Ball Charaktere.
 * <p>
 * Stellt HTTP-Endpunkte zur Verfügung, um Charaktere zu erstellen,
 * zu lesen, zu aktualisieren und zu löschen (CRUD).
 * </p>
 * <p>
 * Diese Klasse kommuniziert mit dem {@link CharacterService}, um
 * alle Geschäftslogiken auszuführen.
 * </p>
 *
 * @author Thierno Hamidou Bah
 * @version 1.0.0
 * @since 2025-07-20
 */
@RestController
@RequestMapping("/api/characters")
@Tag(name = "Characters", description = "CRUD operations for Dragon Ball characters")
public class CharacterController {

    private final CharacterService service;

    /**
     * Konstruktor zur Injektion des CharacterService.
     *
     * @param service Instanz des CharacterService
     */
    public CharacterController(CharacterService service) {
        this.service = service;
    }

    /**
     * Gibt alle gespeicherten Charaktere zurück.
     *
     * @return Liste aller Charaktere
     */

    @GetMapping
    @Operation(summary = "Get all characters", description = "Returns a list of all characters")
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        return ResponseEntity.ok(service.getAllCharacters());
    }

    /**
     * Gibt einen Charakter anhand seiner ID zurück.
     *
     * @param id ID des gesuchten Charakters
     * @return Charakter mit der angegebenen ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get character by ID", description = "Returns a character by its ID")
    @ApiResponse(responseCode = "200", description = "Character found")
    @ApiResponse(responseCode = "404", description = "Character not found")
    public ResponseEntity<CharacterDTO> getCharacterById(
            @Parameter(description = "ID of the character", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.getCharacterById(id));
    }

    /**
     * Gibt einen Charakter anhand seines Namens zurück.
     *
     * @param name Name des gesuchten Charakters
     * @return Gefundener Charakter
     */
    @GetMapping("/by-name/{name}")
    @Operation(summary = "Get character by name", description = "Returns a character with the given name")
    public ResponseEntity<CharacterDTO> getCharacterByName(
            @Parameter(description = "Name of the character", example = "Goku") @PathVariable String name) {
        return ResponseEntity.ok(service.getCharacterByName(name));
    }

    /**
     * Erstellt einen neuen Charakter.
     *
     * @param dto Daten des zu erstellenden Charakters
     * @return Der erstellte Charakter
     */
    @PostMapping
    @Operation(summary = "Create new character", description = "Creates a new character")
    @ApiResponse(responseCode = "201", description = "Character created")
    public ResponseEntity<CharacterDTO> createCharacter(
            @Parameter(description = "Character data", required = true)
            @Valid @RequestBody CharacterDTO dto) {
        CharacterDTO created = service.createCharacter(dto);
        return ResponseEntity.status(201).body(created);
    }

    /**
     * Aktualisiert einen bestehenden Charakter.
     *
     * @param id  ID des zu aktualisierenden Charakters
     * @param dto Neue Daten des Charakters
     * @return Aktualisierter Charakter
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update character", description = "Updates an existing character")
    @ApiResponse(responseCode = "200", description = "Character updated")
    @ApiResponse(responseCode = "404", description = "Character not found")
    public ResponseEntity<CharacterDTO> updateCharacter(
            @Parameter(description = "ID of the character to update", example = "1") @PathVariable Long id,
            @Valid @RequestBody CharacterDTO dto) {
        CharacterDTO updated = service.updateCharacter(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Löscht einen Charakter anhand seiner ID.
     *
     * @param id ID des zu löschenden Charakters
     * @return Leere Antwort mit Status 200
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete character", description = "Deletes a character by its ID")
    @ApiResponse(responseCode = "200", description = "Character deleted")
    @ApiResponse(responseCode = "404", description = "Character not found")
    public ResponseEntity<Void> deleteCharacter(
            @Parameter(description = "ID of the character to delete", example = "1") @PathVariable Long id) {
        service.deleteCharacter(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Gibt alle Charaktere mit einer bestimmten Rasse zurück.
     *
     * @param race Die Rasse der Charaktere
     * @return Liste der passenden Charaktere
     */    @GetMapping("/race/{race}")
    @Operation(summary = "Get characters by race", description = "Returns all characters of a specific race")
    public ResponseEntity<List<CharacterDTO>> getByRace(@PathVariable String race) {
        return ResponseEntity.ok(service.getCharactersByRace(race));
    }

    /**
     * Gibt alle Charaktere mit einem bestimmten Power Level zurück.
     *
     * @param level Power Level (z.B. 900000000000)
     * @return Liste der Charaktere mit exakt diesem Power Level
     */
    @GetMapping("/powerlevel/{level}")
    @Operation(
            summary = "Get characters by power level",
            description = "Returns all characters with a specific power level"
    )
    public ResponseEntity<List<CharacterDTO>> getByPowerLevel(
            @Parameter(
                    description = "Power level",
                    example = "900000000000", // ✅ Beispiel großer Wert
                    schema = @Schema(type = "integer", format = "int64") // ✅ Wichtig!
            )
            @PathVariable(name = "level") Long level // ✅ Großes L oder primitives long geht beides
    ) {
        return ResponseEntity.ok(service.getCharactersByPowerLevel(level));
    }

    /**
     * Gibt alle Charaktere zurück, deren Rasse mit dem angegebenen String
     * übereinstimmt (Gross-/Kleinschreibung wird ignoriert).
     *
     * @param race Gesuchte Rasse
     * @return Liste der passenden Charaktere
     */
    @GetMapping("/race-ignore")
    @Operation(summary = "Get characters by race (ignore case)", description = "Returns characters with case-insensitive race filter")
    public ResponseEntity<List<CharacterDTO>> getCharactersByRaceIgnoreCase(@RequestParam String race) {
        return ResponseEntity.ok(service.getCharactersByRaceIgnoreCase(race));
    }
}
