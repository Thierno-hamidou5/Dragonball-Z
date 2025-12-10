package com.wiss.dragonball.backend.controller;

import com.wiss.dragonball.backend.entity.Character;
import com.wiss.dragonball.backend.entity.User;
import com.wiss.dragonball.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller stellt Endpunkte bereit, damit authentifizierte Nutzer ihre
 * Favoriten ansehen und bearbeiten koennen.
 *
 * Basis-URL: /api/users
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users/favourites
     * Liefert die Favoriten des aktuell eingeloggten Benutzers.
     */
    @GetMapping("/favourites")
    public List<Character> listFavourites(@AuthenticationPrincipal UserDetails principal) {
        return userService.getFavourites(principal.getUsername());
    }

    /**
     * POST /api/users/favourites/{characterId}
     * Fuegt einen Charakter zur Favoritenliste des eingeloggten Nutzers hinzu.
     */
    @PostMapping("/favourites/{characterId}")
    public void addFavourite(@PathVariable Long characterId,
                             @AuthenticationPrincipal UserDetails principal) {
        userService.addFavourite(principal.getUsername(), characterId);
    }

    /**
     * DELETE /api/users/favourites/{characterId}
     * Entfernt einen Charakter aus der Favoritenliste des eingeloggten Nutzers.
     */
    @DeleteMapping("/favourites/{characterId}")
    public void removeFavourite(@PathVariable Long characterId,
                                @AuthenticationPrincipal UserDetails principal) {
        userService.removeFavourite(principal.getUsername(), characterId);
    }

    /**
     * ADMIN: liefert alle Benutzer der Anwendung.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> listAllUsers() {
        return userService.getAllUsers();
    }
}
