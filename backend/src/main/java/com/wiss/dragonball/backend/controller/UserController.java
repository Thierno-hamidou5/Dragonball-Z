package com.wiss.dragonball.backend.controller;

import com.wiss.dragonball.backend.entity.Character;
import com.wiss.dragonball.backend.entity.User;
import com.wiss.dragonball.backend.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller stellt Endpunkte bereit, damit authentifizierte Nutzer ihre
 * Favoriten ansehen und bearbeiten koennen. Die Authentifizierung wird ueber
 * {@code @AuthenticationPrincipal} ermittelt; der JWT-Subject muss dabei auf
 * den Benutzernamen gemappt sein.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Liefert die Favoriten des eingeloggten Nutzers. */
    @GetMapping("/me/favourites")
    public List<Character> listFavourites(@AuthenticationPrincipal UserDetails principal) {
        return userService.getFavourites(principal.getUsername());
    }

    /** Fügt einen Charakter zur Favoritenliste des eingeloggten Nutzers hinzu. */
    @PostMapping("/{characterId}/favourite")
    public void addFavourite(@PathVariable Long characterId,
                             @AuthenticationPrincipal UserDetails principal) {
        userService.addFavourite(principal.getUsername(), characterId);
    }

    /** Entfernt einen Charakter aus der Favoritenliste des eingeloggten Nutzers. */
    @DeleteMapping("/{characterId}/favourite")
    public void removeFavourite(@PathVariable Long characterId,
                                @AuthenticationPrincipal UserDetails principal) {
        userService.removeFavourite(principal.getUsername(), characterId);
    }

    /** ADMIN: Liefert alle Benutzer der Anwendung. */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> listAllUsers() {
        return userService.getAllUsers();
    }
}
