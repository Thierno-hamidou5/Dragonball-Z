package com.wiss.dragonball.backend.controller;

import com.wiss.dragonball.backend.entity.Role;
import com.wiss.dragonball.backend.entity.User;
import com.wiss.dragonball.backend.service.JwtService;
import com.wiss.dragonball.backend.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Authentifizierungs‑Controller. Stellt Login‑ und Registrierungs‑Endpunkte bereit und
 * generiert dabei JWTs. Die Endpunkte liegen unter /api/auth/**.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Authentifiziert einen Nutzer und liefert ein JWT sowie Benutzerinformationen zurück.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        // JWT erzeugen
        String token = jwtService.generateToken(principal);

        // Benutzerdaten ermitteln (inkl. ID und Rolle)
        User dbUser = userService.loadUserOrThrow(principal.getUsername());
        TokenResponse response = new TokenResponse();
        response.setToken(token);
        response.setUsername(dbUser.getUsername());
        response.setRole(dbUser.getRole().name());
        response.setUserId(dbUser.getId());

        return ResponseEntity.ok(response);
    }

    /**
     * Registriert einen neuen Nutzer, sofern der Benutzername noch frei ist.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // Prüfen, ob der Benutzername bereits existiert
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(409).body("username already exists");
        }
        // Neue Benutzer bekommen immer die Rolle PLAYER
        User user = userService.createUser(request.getUsername(), request.getPassword(), Role.PLAYER);
        return ResponseEntity.ok("registered " + user.getUsername());
    }

    /**
     * DTO für Login‑Anfragen.
     */
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    /**
     * Response‑DTO für den Login mit JWT‑Token und User‑Infos.
     */
    public static class TokenResponse {
        private String token;
        private String username;
        private String role;
        private Long userId;

        public TokenResponse() {}

        public TokenResponse(String token, String username, String role, Long userId) {
            this.token = token;
            this.username = username;
            this.role = role;
            this.userId = userId;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    /**
     * DTO für Benutzer‑Registrierung.
     */
    public static class RegisterRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
        private Role role = Role.PLAYER;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }
    }
}
