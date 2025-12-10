package com.wiss.dragonball.backend.service;

import com.wiss.dragonball.backend.entity.Character;
import com.wiss.dragonball.backend.entity.Role;
import com.wiss.dragonball.backend.entity.User;
import com.wiss.dragonball.backend.repository.CharacterRepository;
import com.wiss.dragonball.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service für Benutzerverwaltung, Favoritenlisten
 * UND UserDetailsService-Implementation für Spring Security.
 *
 * Spring Security ruft loadUserByUsername(..) auf, um den User
 * aus der Datenbank zu laden (wie im PDF des Lehrers).
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       CharacterRepository characterRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.characterRepository = characterRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Wird von Spring Security verwendet, um einen User anhand des
     * Usernames aus der Datenbank zu laden.
     *
     * @param username Username (z.B. "admin", "player1")
     * @return UserDetails-Objekt (wird vom Security-Kontext verwendet)
     * @throws UsernameNotFoundException wenn kein User existiert
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));

        // Spring-Security-User bauen (Rolle aus Enum Role)
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())  // z.B. "ADMIN" oder "PLAYER"
                .build();
    }

    /**
     * Legt einen neuen Benutzer mit Benutzername, Passwort und Rolle an.
     * Das Passwort wird gehasht gespeichert (BCrypt/DelegatingPasswordEncoder).
     */
    @Transactional
    public User createUser(String username, String rawPassword, Role role) {
        User user = new User(username, passwordEncoder.encode(rawPassword), role);
        return userRepository.save(user);
    }

    /**
     * Liefert den Benutzer zum angegebenen Benutzernamen oder Optional.empty().
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Fügt den Charakter mit der angegebenen ID zu den Favoriten des Nutzers hinzu.
     */
    @Transactional
    public void addFavourite(String username, Long characterId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new NoSuchElementException("Character not found"));

        user.addFavourite(character);
        userRepository.save(user);
    }

    /**
     * Entfernt den Charakter aus den Favoriten des Nutzers.
     */
    @Transactional
    public void removeFavourite(String username, Long characterId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new NoSuchElementException("Character not found"));

        user.removeFavourite(character);
        userRepository.save(user);
    }

    /**
     * Gibt die Favoriten-Liste des Nutzers zurück.
     * Transactional, damit die Lazy-Relationen fuer die JSON-Serialisierung
     * initialisiert sind.
     */
    @Transactional(readOnly = true)
    public List<Character> getFavourites(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return List.copyOf(user.getFavourites());
    }


    /**
     * Liefert alle Nutzer (z.B. für ADMIN-Endpunkte).
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Kleiner Helfer, falls du irgendwo explizit einen User brauchst.
     */
    public User loadUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + username));
    }
}
