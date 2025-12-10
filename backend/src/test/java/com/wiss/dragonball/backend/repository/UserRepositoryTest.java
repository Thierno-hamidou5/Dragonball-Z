package com.wiss.dragonball.backend.repository;

import com.wiss.dragonball.backend.entity.Character;
import com.wiss.dragonball.backend.entity.Role;
import com.wiss.dragonball.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integrationstests für das UserRepository.
 * Es wird eine In‑Memory‑Datenbank verwendet, um die JPA‑Abfragen
 * isoliert zu prüfen.
 */
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CharacterRepository characterRepository;

    /**
     * UR‑01: Prüft, ob findByUsername einen gespeicherten Benutzer findet.
     */
    @Test
    void whenFindByUsername_thenReturnUser() {
        User user = new User("player", "secret", Role.PLAYER);
        entityManager.persistAndFlush(user);

        Optional<User> found = userRepository.findByUsername("player");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("player");
        assertThat(found.get().getRole()).isEqualTo(Role.PLAYER);
    }

    /**
     * UR‑02: Prüft, ob findAllByFavourites_Id alle Benutzer liefert,
     * die einen bestimmten Charakter als Favoriten gespeichert haben.
     */
    @Test
    void whenFindAllByFavouritesId_thenReturnUsersWithCharacter() {
        Character goku = new Character();
        goku.setName("Goku");
        goku.setRace("Saiyan");
        goku.setPowerLevel(9000000000L);
        characterRepository.saveAndFlush(goku);

        User user = new User("player", "secret", Role.PLAYER);
        user.addFavourite(goku);
        userRepository.saveAndFlush(user);

        List<User> usersWithGoku = userRepository.findAllByFavourites_Id(goku.getId());

        assertThat(usersWithGoku).hasSize(1);
        assertThat(usersWithGoku.getFirst().getUsername()).isEqualTo("player");
        assertThat(usersWithGoku.getFirst().getFavourites())
                .anyMatch(character -> character.getName().equals("Goku"));
    }
}
