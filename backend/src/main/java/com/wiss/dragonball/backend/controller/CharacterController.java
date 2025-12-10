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
 * REST‑Controller für Dragon Ball Charaktere.
 */
@RestController
@RequestMapping("/api/characters")
@Tag(name = "Characters", description = "CRUD operations for Dragon Ball characters")
public class CharacterController {

    private final CharacterService service;

    public CharacterController(CharacterService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all characters", description = "Returns a list of all characters")
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        return ResponseEntity.ok(service.getAllCharacters());
    }

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
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Get character by name", description = "Returns a character with the given name")
    public ResponseEntity<CharacterDTO> getCharacterByName(
            @Parameter(description = "Name of the character", example = "Goku") @PathVariable String name) {
        return ResponseEntity.ok(service.getCharacterByName(name));
    }

    @PostMapping
    @Operation(summary = "Create new character", description = "Creates a new character")
    @ApiResponse(responseCode = "201", description = "Character created")
    public ResponseEntity<CharacterDTO> createCharacter(
            @Parameter(description = "Character data", required = true)
            @Valid @RequestBody CharacterDTO dto) {
        CharacterDTO created = service.createCharacter(dto);
        return ResponseEntity.status(201).body(created);
    }

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

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete character", description = "Deletes a character by its ID")
    @ApiResponse(responseCode = "200", description = "Character deleted")
    @ApiResponse(responseCode = "404", description = "Character not found")
    public ResponseEntity<Void> deleteCharacter(
            @Parameter(description = "ID of the character to delete", example = "1") @PathVariable Long id) {
        service.deleteCharacter(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/race/{race}")
    @Operation(summary = "Get characters by race", description = "Returns all characters of a specific race")
    public ResponseEntity<List<CharacterDTO>> getByRace(@PathVariable String race) {
        return ResponseEntity.ok(service.getCharactersByRace(race));
    }

    @GetMapping("/powerlevel/{level}")
    @Operation(
            summary = "Get characters by power level",
            description = "Returns all characters with a specific power level"
    )
    public ResponseEntity<List<CharacterDTO>> getByPowerLevel(
            @Parameter(
                    description = "Power level",
                    example = "900000000000",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long level
    ) {
        return ResponseEntity.ok(service.getCharactersByPowerLevel(level));
    }

    @GetMapping("/race-ignore")
    @Operation(summary = "Get characters by race (ignore case)", description = "Returns characters with case-insensitive race filter")
    public ResponseEntity<List<CharacterDTO>> getCharactersByRaceIgnoreCase(@RequestParam String race) {
        return ResponseEntity.ok(service.getCharactersByRaceIgnoreCase(race));
    }
}
