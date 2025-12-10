package com.wiss.dragonball.backend.repository;

import com.wiss.dragonball.backend.entity.Character;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integrationstest für das {@link CharacterRepository}.
 * <p>
 * Testet JPA-Abfragen gegen eine In-Memory-Testdatenbank mit {@code @DataJpaTest}.
 * </p>
 *
 * <p><strong>Hinweis:</strong> Der Test verwendet das Profil {@code test}.</p>
 *
 * @author Thierno
 * @version 1.0
 */
@DataJpaTest
@ActiveProfiles("test")
public class CharacterRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CharacterRepository characterRepository;

    /**
     * Testet {@link CharacterRepository#findByRace(String)}.
     * <p>
     * Erwartet, dass nach dem Persistieren eines Saiyan-Charakters
     * dieser bei der Abfrage über {@code findByRace("Saiyan")} zurückgegeben wird.
     * </p>
     */
    @Test
    void whenFindByRace_thenReturnSaiyanCharacters() {
        // Vorbereitung
        Character goku = new Character();
        goku.setName("Goku");
        goku.setRace("Saiyan");
        goku.setGender("Male");
        goku.setPowerLevel(9000000000L);
        entityManager.persistAndFlush(goku);

        // Aktion: Suche nach "Saiyan"
        List<Character> result = characterRepository.findByRace("Saiyan");

        // Pruefung: Ergebnis ueberpruefen
        assertThat(result).isNotEmpty();
        assertThat(result.getFirst().getName()).isEqualTo("Goku");
        assertThat(result.getFirst().getRace()).isEqualTo("Saiyan");
    }
}
