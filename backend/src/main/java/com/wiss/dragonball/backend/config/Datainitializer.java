package com.wiss.dragonball.backend.config;

import com.wiss.dragonball.backend.entity.Character;
import com.wiss.dragonball.backend.entity.Role;
import com.wiss.dragonball.backend.entity.User;
import com.wiss.dragonball.backend.repository.CharacterRepository;
import com.wiss.dragonball.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initialisiert Demo-Nutzer und Beispiel-Charaktere beim Starten der App.
 * Praktisch für lokale Tests; Passwörter werden jeweils frisch gehasht.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           CharacterRepository characterRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.characterRepository = characterRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createOrUpdate("player", "player123", Role.PLAYER);
        createOrUpdate("admin", "admin123", Role.ADMIN);
        createOrUpdate("player2", "player222", Role.PLAYER);
        createOrUpdate("player3", "player333", Role.PLAYER);
        seedCharacters();
    }

    private void createOrUpdate(String username, String rawPassword, Role role) {
        userRepository.findByUsername(username).ifPresentOrElse(existing -> {
            existing.setPassword(passwordEncoder.encode(rawPassword));
            existing.setRole(role);
            userRepository.save(existing);
        }, () -> {
            User user = new User(username, passwordEncoder.encode(rawPassword), role);
            userRepository.save(user);
        });
    }

    private void seedCharacters() {
        if (characterRepository.count() > 0) return;

        characterRepository.save(new Character(
                "Goku", "Saiyan", "60,000,000", "90 Septillion", 900000000000L,
                7, false,
                java.util.List.of("Super Saiyan", "Super Saiyan Blue", "Ultra Instinct"),
                null, "Male", "Earth-raised Saiyan warrior",
                "/img/Jiren.webp", "Z Fighter"));

        // TODO: Weitere Charaktere wie Vegeta, Gohan, Frieza, Jiren hinzufügen
    }
}
