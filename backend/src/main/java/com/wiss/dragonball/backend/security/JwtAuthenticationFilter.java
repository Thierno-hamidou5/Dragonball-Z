package com.wiss.dragonball.backend.security;

import com.wiss.dragonball.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT-Authentifizierungsfilter fuer die Dragonball-Anwendung.
 *
 * <p>Prueft bei jeder Anfrage das Bearer-Token im Authorization-Header,
 * validiert es ueber {@link JwtService} und laedt bei gueltigem Token den
 * Benutzer via {@link UserDetailsService}. Bei Erfolg wird der Nutzer im
 * {@link SecurityContextHolder} hinterlegt, damit Spring Security die Anfrage
 * als authentifiziert betrachtet.</p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization-Header holen, z.B. "Bearer <token>"
        final String authHeader = request.getHeader("Authorization");

        // 2. Existiert der Header und beginnt mit "Bearer "?
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Kein Token -> Authentifizierung ueberspringen und Filterkette fortsetzen
            filterChain.doFilter(request, response);
            return;
        }

        // 3. JWT ohne Prefix extrahieren
        final String jwt = authHeader.substring(7);

        // 4. Benutzername aus Token lesen
        final String username = jwtService.extractUsername(jwt);

        // 5. Wenn Nutzer existiert und noch nicht authentifiziert ist
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 6. UserDetails aus Datenbank laden
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 7. Token validieren
            if (jwtService.validateToken(jwt, userDetails)) {
                // 8. AuthenticationToken mit Authorities erzeugen
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 9. Request-Details (IP etc.) anhaengen
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 10. Nutzer im SecurityContext hinterlegen
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. Weiter zum naechsten Filter
        filterChain.doFilter(request, response);
    }
}
