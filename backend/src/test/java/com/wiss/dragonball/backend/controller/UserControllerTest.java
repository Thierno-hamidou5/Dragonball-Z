package com.wiss.dragonball.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiss.dragonball.backend.entity.Character;
import com.wiss.dragonball.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Tests der Web-Schicht fuer {@link UserController}. Es wird geprueft, ob der
 * Controller korrekt an den {@link UserService} delegiert und die erwarteten
 * HTTP-Status und Antworten liefert. Sicherheitsfilter sind deaktiviert, der
 * Authentifizierungskontext kommt ueber {@link WithMockUser}.
 */
@SuppressWarnings({"removal", "deprecation"})
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private com.wiss.dragonball.backend.service.JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "player", roles = "PLAYER")
    void listFavourites_returnsListOfCharacters() throws Exception {
        // Vorbereitung
        Character character = new Character();
        character.setId(1L);
        character.setName("Goku");
        character.setRace("Saiyan");
        character.setUniverse(7);
        when(userService.getFavourites(anyString())).thenReturn(List.of(character));

        // Aktion & Pruefung
        mockMvc.perform(get("/users/me/favourites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Goku"))
                .andExpect(jsonPath("$[0].race").value("Saiyan"))
                .andExpect(jsonPath("$[0].universe").value(7));
    }

    @Test
    @WithMockUser(username = "player", roles = "PLAYER")
    void addFavourite_invokesServiceAndReturnsOk() throws Exception {
        // Vorbereitung
        doNothing().when(userService).addFavourite(anyString(), anyLong());

        // Aktion & Pruefung
        mockMvc.perform(post("/users/1/favourite"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player", roles = "PLAYER")
    void removeFavourite_invokesServiceAndReturnsOk() throws Exception {
        // Vorbereitung
        doNothing().when(userService).removeFavourite(anyString(), anyLong());

        // Aktion & Pruefung
        mockMvc.perform(delete("/users/1/favourite"))
                .andExpect(status().isOk());
    }
}
