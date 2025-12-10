package com.wiss.dragonball.backend.service;

import com.wiss.dragonball.backend.dto.CharacterDTO;
import com.wiss.dragonball.backend.entity.Character;
import com.wiss.dragonball.backend.repository.CharacterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit-Tests für {@link CharacterService} mit Mockito.
 * <p>
 * Diese Tests prüfen das Verhalten der Service-Schicht ohne Verbindung zur echten Datenbank.
 * Das {@link CharacterRepository} wird dabei als Mock verwendet.
 * </p>
 *
 * <p><strong>Testabdeckung:</strong> Alle-Charaktere-Abruf, Suche nach ID und Rasse, Fehlerfall-ID.</p>
 *
 * @author Thierno
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class CharacterServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @InjectMocks
    private CharacterService characterService;

    /**
     * Testet {@link CharacterService#getAllCharacters()}.
     * <p>Erwartet eine Liste von zwei Charakteren aus dem Repository-Mock.</p>
     */
    @Test
    public void whenGetAllCharacters_thenReturnAllCharacters() {
        Character goku = createTestCharacter("Goku", "Saiyan");
        Character vegeta = createTestCharacter("Vegeta", "Saiyan");

        List<Character> mockCharacters = Arrays.asList(goku, vegeta);
        when(characterRepository.findAll()).thenReturn(mockCharacters);

        List<CharacterDTO> result = characterService.getAllCharacters();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Goku");
        assertThat(result.get(1).getName()).isEqualTo("Vegeta");

        verify(characterRepository, times(1)).findAll();
    }

    /**
     * Testet {@link CharacterService#getCharacterById(Long)} mit einer gültigen ID.
     * <p>Erwartet einen DTO mit passendem Namen und ID.</p>
     */
    @Test
    public void whenGetCharacterById_thenReturnCharacter() {
        Long id = 1L;
        Character goku = createTestCharacter("Goku", "Saiyan");
        goku.setId(id);

        when(characterRepository.findById(id)).thenReturn(Optional.of(goku));

        CharacterDTO result = characterService.getCharacterById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Goku");

        verify(characterRepository, times(1)).findById(id);
    }

    /**
     * Testet {@link CharacterService#getCharacterById(Long)} mit ungültiger ID.
     * <p>Erwartet eine {@link RuntimeException}, wenn der Charakter nicht gefunden wird.</p>
     */
    @Test
    public void whenGetCharacterByInvalidId_thenThrowException() {
        Long invalidId = 999L;
        when(characterRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> characterService.getCharacterById(invalidId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Character with ID: " + invalidId + " not found");

        verify(characterRepository, times(1)).findById(invalidId);
    }

    /**
     * Testet {@link CharacterService#getCharactersByRace(String)}.
     * <p>Erwartet eine gefilterte Liste mit einem Charakter der gewünschten Rasse.</p>
     */
    @Test
    public void whenGetCharactersByRace_thenReturnFilteredCharacters() {
        String race = "Saiyan";
        Character goku = createTestCharacter("Goku", race);
        List<Character> mockList = List.of(goku);

        when(characterRepository.findByRace(race)).thenReturn(mockList);

        List<CharacterDTO> result = characterService.getCharactersByRace(race);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRace()).isEqualTo(race);

        verify(characterRepository, times(1)).findByRace(race);
    }

    /**
     * Hilfsmethode zum Erzeugen eines {@link Character}-Testobjekts.
     *
     * @param name Name des Charakters
     * @param race Rasse des Charakters
     * @return Instanz eines {@link Character} mit Standardwerten
     */
    private Character createTestCharacter(String name, String race) {
        Character c = new Character();
        c.setName(name);
        c.setRace(race);
        c.setKi("100000");
        c.setMaxKi("999999");
        c.setGender("Male");
        c.setAffiliation("Z Fighter");
        c.setPowerLevel(900000000L);
        c.setUniverse(7);
        c.setVillain(false);
        return c;
    }

    /**
     * Testet {@link CharacterService#existsByNameAndRace(String, String)}.
     * <p>
     * Erwartet {@code true}, wenn ein Charakter mit dem angegebenen Namen und der Rasse existiert.
     * Der Repository-Call wird per Mockito simuliert.
     * </p>
     */
    @Test
    public void whenExistsByNameAndRace_thenReturnTrue() {
        String name = "Goku";
        String race = "Saiyan";

        when(characterRepository.existsByNameAndRace(name, race)).thenReturn(true);

        boolean exists = characterService.existsByNameAndRace(name, race);

        assertThat(exists).isTrue();
        verify(characterRepository, times(1)).existsByNameAndRace(name, race);
    }

}
