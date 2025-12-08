package com.wiss.dragonball.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiss.dragonball.backend.dto.CharacterDTO;
import com.wiss.dragonball.backend.service.CharacterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integrationstest für den {@link CharacterController} mit {@link MockMvc}.
 * <p>
 * Testet die HTTP-Endpunkte der REST-API isoliert vom Service und der Datenbank,
 * indem der {@link CharacterService} gemockt wird.
 * </p>
 *
 * <p>Voraussetzung: {@code @WebMvcTest} lädt nur die Web-Schicht.</p>
 *
 * @author Thierno
 * @version 1.0
 */
@WebMvcTest(CharacterController.class)
public class CharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CharacterService characterService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Testet den Endpunkt {@code GET /api/characters}.
     * <p>
     * Erwartet eine Liste von Charakteren im JSON-Format.
     * </p>
     *
     * @throws Exception bei Fehler im MockMvc-Aufruf
     */
    @Test
    public void whenGetAllCharacters_thenReturnJsonArray() throws Exception {
        CharacterDTO goku = createCharacterDTO(1L, "Goku", "Saiyan");
        CharacterDTO vegeta = createCharacterDTO(2L, "Vegeta", "Saiyan");
        List<CharacterDTO> allCharacters = Arrays.asList(goku, vegeta);

        when(characterService.getAllCharacters()).thenReturn(allCharacters);

        mockMvc.perform(get("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Goku")))
                .andExpect(jsonPath("$[1].name", is("Vegeta")));
    }

    /**
     * Testet den Endpunkt {@code GET /api/characters/race/{race}}.
     * <p>
     * Erwartet eine gefilterte Liste von Charakteren anhand der Rasse.
     * </p>
     *
     * @throws Exception bei Fehler im MockMvc-Aufruf
     */
    @Test
    public void whenGetCharactersByRace_thenReturnFilteredCharacters() throws Exception {
        CharacterDTO goku = createCharacterDTO(1L, "Goku", "Saiyan");
        List<CharacterDTO> saiyanCharacters = List.of(goku);

        when(characterService.getCharactersByRace("Saiyan")).thenReturn(saiyanCharacters);

        mockMvc.perform(get("/api/characters/race/Saiyan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].race", is("Saiyan")));
    }

    /**
     * Hilfsmethode zum Erstellen eines einfachen {@link CharacterDTO}-Objekts für Tests.
     *
     * @param id   ID des Charakters
     * @param name Name des Charakters
     * @param race Rasse des Charakters
     * @return Ein vorbereitetes {@link CharacterDTO}-Testobjekt
     */
    private CharacterDTO createCharacterDTO(Long id, String name, String race) {
        CharacterDTO dto = new CharacterDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setRace(race);
        dto.setPowerLevel(900000000);
        dto.setVillain(false);
        return dto;
    }
}
