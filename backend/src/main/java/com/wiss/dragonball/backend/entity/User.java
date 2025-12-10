package com.wiss.dragonball.backend.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity fuer einen Benutzer der Anwendung. Nutzer koennen Spieler oder Admins sein
 * und pflegen eine Favoritenliste von Charakteren. Falls im Projekt bereits eine
 * User-Entitaet existiert, sollten die Felder erweitert und kein Duplikat erzeugt werden.
 */
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Many-to-many Verknuepfung zwischen Nutzern und ihren Lieblings-Charakteren.
     * Erstellt eine Join-Tabelle {@code user_favourite}. Der Tabellenname kann
     * bei Bedarf angepasst werden.
     */
    @ManyToMany
    @JoinTable(
            name = "user_favourite",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private Set<Character> favourites = new HashSet<>();

    // Konstruktoren
    public User() { }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Set<Character> getFavourites() { return favourites; }
    public void setFavourites(Set<Character> favourites) { this.favourites = favourites; }

    /**
     * Fuegt einen Charakter den Favoriten hinzu. Keine Wirkung, falls bereits enthalten.
     */
    public void addFavourite(Character character) {
        this.favourites.add(character);
    }

    /**
     * Entfernt einen Charakter aus den Favoriten. Gibt {@code true} zurueck,
     * falls der Charakter vorhanden war und entfernt wurde.
     */
    public boolean removeFavourite(Character character) {
        return this.favourites.remove(character);
    }
}
