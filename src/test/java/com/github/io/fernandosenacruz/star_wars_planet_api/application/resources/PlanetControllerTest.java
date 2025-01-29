package com.github.io.fernandosenacruz.star_wars_planet_api.application.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.io.fernandosenacruz.star_wars_planet_api.application.services.PlanetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.github.io.fernandosenacruz.star_wars_planet_api.common.PlanetConstants.INVALID_PLANET;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static com.github.io.fernandosenacruz.star_wars_planet_api.common.PlanetConstants.PLANET;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {
    @MockBean
    private PlanetService planetService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createPlanet_WithVValidData_ReturnsCreated() throws Exception {
        when(planetService.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET))
                                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
               .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void createPlanet_WithVInvalidData_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(INVALID_PLANET))
                                        .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistingPlanet_ReturnsConflict() throws Exception {
        when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET))
                                        .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isConflict());
    }

    @Test
    public void getPlanet_ById_ReturnsPlanet() throws Exception {
        Long id = anyLong();
        when(planetService.get(id)).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/{id}", id))
               .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByNonexistentId_ReturnsNotFound() throws Exception {
        Long id = anyLong();
        when(planetService.get(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/planets/{id}", id))
               .andExpect(status().isNotFound());
    }
}
