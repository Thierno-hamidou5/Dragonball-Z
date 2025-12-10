package com.wiss.dragonball.backend.service;

import com.wiss.dragonball.backend.entity.Character;
import com.wiss.dragonball.backend.entity.Role;
import com.wiss.dragonball.backend.entity.User;
import com.wiss.dragonball.backend.repository.CharacterRepository;
import com.wiss.dragonball.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-Tests fuer {@link UserService}. Prueft, dass Passwoerter gehasht werden
 * und die Favoriten-Verwaltung die Liste korrekt anpasst.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    /**
     * Stellt sicher, dass Passwoerter vor dem Speichern gehasht werden und das
     * Repository den ausgefuellten Nutzer erhaelt.
     */
    @Test
    void createUser_encodesPasswordAndSavesUser() {
        // Vorbereitung
        when(passwordEncoder.encode("secret")).thenReturn("hashedSecret");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Aktion
        User user = userService.createUser("player", "secret", Role.PLAYER);

        // Pruefung
        verify(passwordEncoder).encode("secret");
        verify(userRepository).save(any(User.class));
        assertEquals("player", user.getUsername());
        assertEquals(Role.PLAYER, user.getRole());
    }

    /**
     * Stellt sicher, dass ein hinzugefuegter Favorit mit dem Nutzer verknuepft
     * und ueber das Repository gespeichert wird.
     */
    @Test
    void addFavourite_addsCharacterToUserFavourites() {
        // Vorbereitung
        User user = new User("player", "hashed", Role.PLAYER);
        Character character = new Character();
        character.setId(1L);
        when(userRepository.findByUsername("player")).thenReturn(Optional.of(user));
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Aktion
        userService.addFavourite("player", 1L);

        // Pruefung
        assertEquals(1, user.getFavourites().size(), "Charakter sollte zu den Favoriten hinzugefuegt werden");
    }

    /**
     * Stellt sicher, dass ein Favorit entfernt wird und die aktualisierte
     * Entitaet gespeichert wird.
     */
    @Test
    void removeFavourite_removesCharacterFromUserFavourites() {
        // Vorbereitung
        User user = new User("player", "hashed", Role.PLAYER);
        Character character = new Character();
        character.setId(2L);
        user.addFavourite(character);
        when(userRepository.findByUsername("player")).thenReturn(Optional.of(user));
        when(characterRepository.findById(2L)).thenReturn(Optional.of(character));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Aktion
        userService.removeFavourite("player", 2L);

        // Pruefung
        assertEquals(0, user.getFavourites().size(), "Charakter sollte aus den Favoriten entfernt werden");
    }
}
